package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.*;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceTests {

    private static Service service;

    @BeforeEach
    public void setup() {
        try {
            service = new Service();
        } catch (Exception error) {
            fail();
        }
    }

    public String registerGoodUser() {
        AuthData auth = null;
        var registerData = new UserData("username", "password", "email");
        try {
            auth = service.registerUser(registerData);
        } catch (DataAccessException error) {
            fail();
        }
        return auth.authToken();
    }

    public HashSet<GameData> createGoodGames(int num, String authToken) {
        HashSet<GameData> games = new HashSet<>();
        for (int i = 1; i <= num; i++) {
            var validGameRequest = new RequestCreateGame(authToken, Integer.toString(i), null, null);
            games.add(new GameData(i, null, null, Integer.toString(i), new ChessGame()));
            try {
                service.createGame(validGameRequest);
            } catch (DataAccessException error) {
                fail();
            }
        }
        return games;
    }

    @Test
    void clear() {
        try {
            service.clear();
        } catch (DataAccessException error) {
            fail();
        }
    }

    @Test
    void registerTwice() {
        var goodData = new UserData("username", "password", "email");
        AuthData result = null;
        try {
            result = service.registerUser(goodData);
        } catch (DataAccessException error) {
            fail();
        }
        assertEquals("username", result.username());

        try {
            service.registerUser(goodData);
        } catch (DataAccessException error) {
            assertEquals("already taken", error.getMessage());
            return;
        }
        fail();
    }

    @Test
    void badRegister() {
        var badData = new UserData(null, "password", "email");
        try {
            service.registerUser(badData);
        } catch (DataAccessException error) {
            assertEquals("bad request", error.getMessage());
            return;
        }
        fail();
    }

    @Test
    void loginUser() {
        registerGoodUser();

        AuthData auth1 = null;
        var goodLoginData = new LoginData("username", "password");
        try {
            auth1 = service.loginUser(goodLoginData);
        } catch (DataAccessException error) {
            fail();
        }
        assertNotNull(auth1.authToken());

        AuthData auth2 = null;
        try {
            auth2 = service.loginUser(goodLoginData);
        } catch (DataAccessException error) {
            fail();
        }
        assertNotEquals(auth1, auth2);
    }

    @Test
    void badPasswordLogin() {
        registerGoodUser();

        var badPasswordLoginData = new LoginData("username", null);
        try {
            service.loginUser(badPasswordLoginData);
        } catch (DataAccessException error) {
            assertEquals("bad request", error.getMessage());
            return;
        }
        fail();
    }

    @Test
    void badUsernameLogin() {
        registerGoodUser();

        var badPasswordLoginData = new LoginData("username1", "password");
        try {
            service.loginUser(badPasswordLoginData);
        } catch (DataAccessException error) {
            assertEquals("unauthorized", error.getMessage());
            return;
        }
        fail();
    }

    @Test
    void logoutUser() {
        String authToken = registerGoodUser();
        try {
            service.logoutUser(authToken);
        } catch (DataAccessException error) {
            fail();
        }
    }

    @Test
    void createValidGame() {
        String authToken = registerGoodUser();
        var validGameRequest = new RequestCreateGame(authToken, "game", null, null);
        int gameID = 0;
        try {
            gameID = service.createGame(validGameRequest);
        } catch (DataAccessException error) {
            fail();
        }
        assertEquals(1, gameID);
    }

    @Test
    void createBadGame() {
        String authToken = registerGoodUser();
        var invalidGameRequest = new RequestCreateGame(authToken, null, null, null);
        int gameID = 0;
        try {
            gameID = service.createGame(invalidGameRequest);
        } catch (DataAccessException error) {
            assertEquals("bad request", error.getMessage());
            return;
        }
        fail();
    }

    @Test
    void listGames() {
        String authToken = registerGoodUser();
        HashSet<GameData> expectedGames = createGoodGames(5, authToken);
        GameListData resultGames = null;
        try {
            resultGames = service.listGames(authToken);
        } catch (DataAccessException error) {
            fail();
        }
        assertEquals(expectedGames, resultGames.games());
    }

    @Test
    void joinGame() {
        String authToken = registerGoodUser();
        createGoodGames(1, authToken);
        try {
            service.joinGame(new RequestJoinGame(authToken, "WHITE", 1));
        } catch (DataAccessException error) {
            fail();
        }

        GameListData game = null;
        try {
            game = service.listGames(authToken);
        } catch (DataAccessException error) {
            fail();
        }
    }

}
