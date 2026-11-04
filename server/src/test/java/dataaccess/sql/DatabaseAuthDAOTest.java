package dataaccess.sql;

import dataaccess.DataAccessException;
import model.AuthData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class DatabaseAuthDAOTest {

    private final DatabaseAuthDAO databaseAuthDAO = new DatabaseAuthDAO();

    public DatabaseAuthDAOTest() throws DataAccessException {
    }

    @BeforeEach
    public void setup() {
        try {
            databaseAuthDAO.clear();
        } catch (DataAccessException error) {
            fail();
        }
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
}
