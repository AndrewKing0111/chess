package service;

import dataaccess.DataAccessException;
import model.UserData;
import org.eclipse.jetty.util.log.Log;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.Service;
import model.*;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceTests {

    private static Service service;

    @BeforeEach
    public void setup() {
        service = new Service();
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

}
