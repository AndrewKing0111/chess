package service;

import dataaccess.DataAccessException;
import model.UserData;
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

    @Test
    void clear() {
        try {
            service.clear();
        } catch (DataAccessException error) {
            assertTrue(false);
        }
        assertTrue(true);
    }

    @Test
    void registerUser() {
        var service = new Service();
        var goodData = new UserData("username", "password", "email");
        var badData = new UserData("", "password", "email");
        AuthData result = null;
        try {
            result = service.registerUser(goodData);
        } catch (DataAccessException error) {
            fail();
        }
        assertEquals("username", result.username());

        try {
            service.registerUser(badData);
        } catch (DataAccessException error) {
            fail();
        }
    }

    @Test
    void loginUser() {

    }

}
