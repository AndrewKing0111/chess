package service;

import dataaccess.DatabaseDAOFactory;
import model.LoginData;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

public class DatabaseServiceTests {

    private static Service service;

    @BeforeEach
    public void setup() {
        try {
            service = new Service(new DatabaseDAOFactory());
            service.clear();
        } catch (Exception error) {
            fail();
        }
    }

    @Test
    void clear() {
        try {
            service.clear();
        } catch (Exception error) {
            fail();
        }
    }

    @Test
    void registerUser() {
        try {
            service.registerUser(new UserData("username", "password", "email"));
        } catch (Exception error) {
            fail();
        }
    }

    @Test
    void loginUser() {
        try {
            service.registerUser(new UserData("username", "password", "email"));
            service.loginUser(new LoginData("username", "password"));
        } catch (Exception error) {
            fail();
        }
    }
}
