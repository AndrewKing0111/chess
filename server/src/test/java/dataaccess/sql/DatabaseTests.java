package dataaccess.sql;

import chess.ChessGame;
import dataaccess.DataAccessException;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class DatabaseTests {

    private final DatabaseAuthDAO databaseAuthDAO = new DatabaseAuthDAO();
    private final DatabaseGameDAO databaseGameDAO =  new DatabaseGameDAO();
    private final DatabaseUserDAO databaseUserDAO = new DatabaseUserDAO();

    public DatabaseTests() throws DataAccessException {
        DatabaseManager.createDatabase();
    }

    AuthData createValidAuth() {
        AuthData auth = null;
        try {
            auth = databaseAuthDAO.createAuth("username");
        } catch (DataAccessException error) {
            fail();
        }
        return auth;
    }

    public GameData createValidGame() {
        RequestCreateGame request = new RequestCreateGame("authToken", "Test Game", null, null);
        int gameID = -1;
        try {
            gameID = databaseGameDAO.createGame(request);
        } catch (DataAccessException error) {
            fail();
        }
        return new GameData(gameID, request.whiteUserName(), request.blackUserName(), request.gameName(), new ChessGame());
    }

    public void registerUserValidUser() {
        var request = new UserData("username", "password", "email");
        try {
            databaseUserDAO.registerUser(request);
        } catch (DataAccessException error) {
            fail();
        }
    }

    @BeforeEach
    public void setup() {
        try {
            databaseAuthDAO.clear();
            databaseGameDAO.clear();
            databaseUserDAO.clear();
        } catch (DataAccessException error) {
            fail();
        }
    }

    @Test
    void createAuthBadUsername() {
        try {
            databaseAuthDAO.createAuth(null);
            fail();
        } catch (DataAccessException error) {
            assertEquals("bad request", error.getMessage());
        }
    }

    @Test
    void allowMultipleCreateAuth() {
        AuthData auth = null;
        try {
            auth = databaseAuthDAO.createAuth("username");
        } catch (DataAccessException error) {
            fail();
        }
        assertEquals("username", auth.username());

        try {
            auth = databaseAuthDAO.createAuth("username");
        } catch (DataAccessException error) {
            fail();
        }
        assertEquals("username", auth.username());
    }

    @Test
    void logoutExistingAuth() {
        AuthData auth = createValidAuth();
        try {
            databaseAuthDAO.logoutUser(auth.authToken());
        } catch (DataAccessException error) {
            fail();
        }
    }

    @Test
    void logoutInvalidAuthToken() {
        createValidAuth();
        try {
            databaseAuthDAO.logoutUser("gibberish");
            fail();
        } catch (DataAccessException error) {
            assertEquals("unauthorized", error.getMessage());
        }
    }

    @Test
    void logoutNullAuthToken() {
        try {
            databaseAuthDAO.logoutUser(null);
            fail();
        } catch (DataAccessException error) {
            assertEquals("bad request", error.getMessage());
        }
    }

    @Test
    void getAuthValidAuthToken() {
        AuthData auth = createValidAuth();
        AuthData returnedAuth = null;
        try {
            returnedAuth = databaseAuthDAO.getAuth(auth.authToken());
        } catch (DataAccessException error) {
            fail();
        }
        assertEquals(auth, returnedAuth);
    }

    @Test
    void getAuthInvalidAuthToken() {
        try {
            databaseAuthDAO.getAuth("gibberish");
            fail();
        } catch (DataAccessException error) {
            assertEquals("unauthorized", error.getMessage());
        }
    }

    @Test
    void getAuthNullAuthToken() {
        try {
            databaseAuthDAO.getAuth(null);
            fail();
        } catch (DataAccessException error) {
            assertEquals("bad request", error.getMessage());
        }
    }

    @Test
    void getUsernameValidAuthToken() {
        AuthData auth = createValidAuth();
        String returnedUsername = null;
        try {
            returnedUsername = databaseAuthDAO.getUsername(auth.authToken());
        } catch (DataAccessException error) {
            fail();
        }
        assertEquals(auth.username(), returnedUsername);
    }

    @Test
    void getUsernameInvalidAuthToken() {
        try {
            databaseAuthDAO.getUsername("gibberish");
            fail();
        } catch (DataAccessException error) {
            assertEquals("unauthorized", error.getMessage());
        }
    }

    @Test
    void getUsernameNullAuthToken() {
        try {
            databaseAuthDAO.getUsername(null);
            fail();
        } catch (DataAccessException error) {
            assertEquals("bad request", error.getMessage());
        }
    }

    @Test
    void createGame() {
        RequestCreateGame request = new RequestCreateGame("authToken", "Test Game", null, null);
        int gameID = -1;
        try {
            gameID = databaseGameDAO.createGame(request);
        } catch (DataAccessException error) {
            fail();
        }
        assertEquals(1, gameID);
    }

    @Test
    void createGameBadAuthTokenGameName() {
        RequestCreateGame badAuthToken = new RequestCreateGame(null, "Test Game", null, null);
        try {
            databaseGameDAO.createGame(badAuthToken);
            fail();
        } catch (DataAccessException error) {
            assertEquals("bad request", error.getMessage());
        }

        RequestCreateGame badGameName = new RequestCreateGame("authToken", null, null, null);
        try {
            databaseGameDAO.createGame(badGameName);
            fail();
        } catch (DataAccessException error) {
            assertEquals("bad request", error.getMessage());
        }
    }

    @Test
    void listGamesOneGame() {
        var expectedGameList = new HashSet<GameData>();
        expectedGameList.add(createValidGame());
        GameListData returnedGameList = null;
        try {
            returnedGameList = databaseGameDAO.listGames("authToken");
        } catch (DataAccessException error) {
            fail();
        }
        assertEquals(returnedGameList.games(), expectedGameList);
    }

    @Test
    void listGamesMultipleGames() {
        var expectedGameList = new HashSet<GameData>();
        expectedGameList.add(createValidGame());
        expectedGameList.add(createValidGame());
        expectedGameList.add(createValidGame());
        GameListData returnedGameList = null;
        try {
            returnedGameList = databaseGameDAO.listGames("authToken");
        } catch (DataAccessException error) {
            fail();
        }
        assertEquals(returnedGameList.games(), expectedGameList);
    }

    @Test
    void listGamesNoGames() {
        var expectedGameList = new HashSet<GameData>();
        GameListData returnedGameList = null;
        try {
            returnedGameList = databaseGameDAO.listGames("authToken");
        } catch (DataAccessException error) {
            fail();
        }
        assertEquals(returnedGameList.games(), expectedGameList);
    }

    @Test
    void joinGameValidRequest() {
        var game = createValidGame();
        try {
            databaseGameDAO.joinGame(new RequestJoinGame("authToken", "WHITE", game.gameID()), "username");
        } catch (DataAccessException error) {
            fail();
        }
    }

    @Test
    void joinGameInvalidRequest() {
        var game = createValidGame();
        try {
            databaseGameDAO.joinGame(new RequestJoinGame("authToken", "WHITE", -1), "username");
            fail();
        } catch (DataAccessException error) {
            assertEquals("bad request", error.getMessage());
        }

        try {
            databaseGameDAO.joinGame(new RequestJoinGame("authToken", null, game.gameID()), "username");
            fail();
        } catch (DataAccessException error) {
            assertEquals("bad request", error.getMessage());
        }
    }

    @Test
    void joinGameStealColor() {
        var game = createValidGame();
        try {
            databaseGameDAO.joinGame(new RequestJoinGame("authToken", "WHITE", game.gameID()), "username");
        } catch (DataAccessException error) {
            fail();
        }

        try {
            databaseGameDAO.joinGame(new RequestJoinGame("authToken", "WHITE", game.gameID()), "username1");
            fail();
        } catch (DataAccessException error) {
            assertEquals("already taken", error.getMessage());
        }

        try {
            databaseGameDAO.joinGame(new RequestJoinGame("authToken", "BLACK", game.gameID()), "username");
        } catch (DataAccessException error) {
            fail();
        }

        try {
            databaseGameDAO.joinGame(new RequestJoinGame("authToken", "BLACK", game.gameID()), "username1");
            fail();
        } catch (DataAccessException error) {
            assertEquals("already taken", error.getMessage());
        }
    }

    @Test
    void joinGameBadColor() {
        var game = createValidGame();
        try {
            databaseGameDAO.joinGame(new RequestJoinGame("authToken", "GREEN", game.gameID()), "username");
            fail();
        } catch (DataAccessException error) {
            assertEquals("bad request", error.getMessage());
        }
    }

    @Test
    void getUserByUsernameValidUsername() {
        registerUserValidUser();
        try {
            databaseUserDAO.getUserByUsername("username");
        } catch (DataAccessException error) {
            fail();
        }
    }

    @Test
    void getUserByUsernameInvalidUsername() {
        registerUserValidUser();
        try {
            databaseUserDAO.getUserByUsername("gibberish");
            fail();
        } catch (DataAccessException error) {
            assertEquals("bad request", error.getMessage());
        }
    }

    @Test
    void getUserByUsernameNullUsername() {
        registerUserValidUser();
        try {
            databaseUserDAO.getUserByUsername(null);
            fail();
        } catch (DataAccessException error) {
            assertEquals("bad request", error.getMessage());
        }
    }

    @Test
    void registerUserNullData() {
        var request = new UserData(null, "password", "email");
        try {
            databaseUserDAO.registerUser(request);
            fail();
        } catch (DataAccessException error) {
            assertEquals("bad request", error.getMessage());
        }
    }

    @Test
    void registerUserDuplicateUsername() {
        registerUserValidUser();
        var request = new UserData("username", "password", "email");
        try {
            databaseUserDAO.registerUser(request);
            fail();
        } catch (DataAccessException error) {
            assertEquals("already taken", error.getMessage());
        }
    }

    @Test
    void loginUserValidRequest() {
        registerUserValidUser();
        var request = new LoginData("username", "password");
        try {
            databaseUserDAO.loginUser(request);
        } catch (DataAccessException error) {
            fail();
        }
    }

    @Test
    void loginUserInvalidRequest() {
        registerUserValidUser();
        var request = new LoginData("username", null);
        try {
            databaseUserDAO.loginUser(request);
            fail();
        } catch (DataAccessException error) {
            assertEquals("bad request", error.getMessage());
        }
    }

    @Test
    void loginUserInvalidPassword() {
        registerUserValidUser();
        var request = new LoginData("username", "gibberish");
        try {
            databaseUserDAO.loginUser(request);
            fail();
        } catch (DataAccessException error) {
            assertEquals("unauthorized", error.getMessage());
        }
    }

    @Test
    void loginUserInvalidUsername() {
        registerUserValidUser();
        var request = new LoginData("gibberish", "password");
        try {
            databaseUserDAO.loginUser(request);
            fail();
        } catch (DataAccessException error) {
            assertEquals("unauthorized", error.getMessage());
        }
    }
}
