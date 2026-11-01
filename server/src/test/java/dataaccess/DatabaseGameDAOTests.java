package dataaccess;

import dataaccess.database.DatabaseGameDAO;
import model.RequestCreateGame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class DatabaseGameDAOTests {

    private final DatabaseGameDAO databaseGameDAO =  new DatabaseGameDAO();

    public DatabaseGameDAOTests() throws DataAccessException {}

    @BeforeEach
    public void setup() {
        try {
            databaseGameDAO.clear();
        }  catch (DataAccessException error) {
            fail();
        }
    }

    public int createValidGame() {
        RequestCreateGame request = new RequestCreateGame("authToken", "Test Game", null, null);
        int gameID = -1;
        try {
             gameID = databaseGameDAO.createGame(request);
        } catch (DataAccessException error) {
            fail();
        }
        return gameID;
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
}
