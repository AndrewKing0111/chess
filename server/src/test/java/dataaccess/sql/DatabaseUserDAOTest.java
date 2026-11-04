package dataaccess.sql;

import dataaccess.DataAccessException;
import model.LoginData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class DatabaseUserDAOTest {

    private final DatabaseUserDAO databaseUserDAO = new DatabaseUserDAO();

    public DatabaseUserDAOTest() throws DataAccessException {}

    public void registerUserValidUser() {
        var request = new UserData("username", "password", "email");
        try {
            databaseUserDAO.registerUser(request);
        } catch (DataAccessException error) {
            fail();
        }
    }

    @BeforeEach
    public void setUp() {
        try {
            databaseUserDAO.clear();
        } catch (DataAccessException error) {
            fail();
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
