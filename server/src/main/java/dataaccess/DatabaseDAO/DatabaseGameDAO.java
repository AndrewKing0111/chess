package dataaccess.DatabaseDAO;

import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.GameData;
import model.GameListData;
import model.RequestCreateGame;
import model.RequestJoinGame;

public class DatabaseGameDAO implements GameDAO {

    public DatabaseGameDAO() throws DataAccessException {
        var db = new DatabaseManager();
        try {
            db.configureDatabase("""
            CREATE TABLE IF NOT EXISTS games (
            `gameID` int NOT NULL AUTO_INCREMENT,
            `whiteUsername` varchar(256) NOT NULL,
            `blackUsername` varchar(256) NOT NULL,
            `gameName` varchar(256) NOT NULL,
            `game` TEXT NOT NULL,
            
            INDEX(gameID)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """);
        } catch (Exception error) {
            throw new DataAccessException("the server failed to initialize the database");
        }
    }

    @Override
    public int createGame(RequestCreateGame gameRequest) throws DataAccessException {
        return 0;
    }

    @Override
    public GameListData listGames(String authToken) throws DataAccessException {
        return null;
    }

    @Override
    public void joinGame(RequestJoinGame joinRequest, String username) throws DataAccessException {

    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return null;
    }

    @Override
    public void updateGame(int gameID, String gameString) throws DataAccessException {

    }

    @Override
    public void clear() throws DataAccessException {

    }
}
