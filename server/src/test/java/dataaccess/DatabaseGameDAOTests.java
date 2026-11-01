package dataaccess;

import chess.ChessGame;
import dataaccess.database.DatabaseGameDAO;
import model.GameData;
import model.GameListData;
import model.RequestCreateGame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

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
    void join
}
