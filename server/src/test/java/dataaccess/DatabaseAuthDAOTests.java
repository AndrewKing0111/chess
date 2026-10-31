package dataaccess;

import dataaccess.database.DatabaseAuthDAO;
import model.AuthData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class DatabaseAuthDAOTests {

    private DatabaseAuthDAO databaseAuthDAO = new DatabaseAuthDAO();

    public DatabaseAuthDAOTests() throws DataAccessException {
    }

    @BeforeEach
    public void setup() {
        try {
            databaseAuthDAO.clear();
        } catch (DataAccessException error) {
            fail();
        }
    }

    @Test
    void createValidAuth() {
        AuthData auth = null;
        try {
            auth = databaseAuthDAO.createAuth("username");
        } catch (DataAccessException error) {
            fail();
        }
        assertEquals("username", auth.username());
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
}
