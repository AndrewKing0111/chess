package dataaccess.DatabaseDAO;

import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.GameData;
import model.GameListData;
import model.RequestCreateGame;
import model.RequestJoinGame;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseGameDAO implements GameDAO {

    public DatabaseGameDAO() throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String configStatement = """
                    CREATE TABLE IF NOT EXISTS games (
                      `gameID` int NOT NULL AUTO_INCREMENT,
                      `whiteUsername` varchar(256) NOT NULL,
                      `blackUsername` varchar(256) NOT NULL,
                      `gameName` varchar(256) NOT NULL,
                      `game` TEXT NOT NULL,
                    
                    INDEX(gameID)
                    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
                    """;
            try (var preparedStatement = conn.prepareStatement(configStatement)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new DataAccessException("the server experienced an internal error");
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
