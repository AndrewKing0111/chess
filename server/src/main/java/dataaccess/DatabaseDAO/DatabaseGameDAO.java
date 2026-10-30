package dataaccess.DatabaseDAO;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.GameData;
import model.GameListData;
import model.RequestCreateGame;
import model.RequestJoinGame;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseGameDAO implements GameDAO {

    private int counter = 1;

    public DatabaseGameDAO() throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String configStatement = """
                    CREATE TABLE IF NOT EXISTS games (
                      `id` int NOT NULL AUTO_INCREMENT,
                      `gameID` int NOT NULL,
                      `whiteUsername` varchar(256) NULL,
                      `blackUsername` varchar(256) NULL,
                      `gameName` varchar(256) NOT NULL,
                      `game` TEXT NOT NULL,
                      PRIMARY KEY (`id`),
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
        String authToken = gameRequest.authToken();
        String gameName = gameRequest.gameName();
        String whiteUserName = gameRequest.whiteUserName();
        String blackUserName = gameRequest.blackUserName();

        if ((authToken == null) || (gameName == null)) {
            throw new DataAccessException("bad request");
        }

        var game = new Gson().toJson(new ChessGame());

        try (Connection conn = DatabaseManager.getConnection()) {
            var preparedStatement = conn.prepareStatement("INSERT INTO games (gameID, whiteUsername, blackUsername, " +
                    "gameName, game) VALUES (?, ?, ?, ?, ?)");
            preparedStatement.setInt(1, counter);
            preparedStatement.setString(2, whiteUserName);
            preparedStatement.setString(3, blackUserName);
            preparedStatement.setString(4, gameName);
            preparedStatement.setString(5, game);
            preparedStatement.executeUpdate();
        } catch (SQLException error) {
            throw new DataAccessException("the server experienced an internal error");
        }
        return counter++;
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
        try (Connection conn = DatabaseManager.getConnection()) {
            var preparedStatement = conn.prepareStatement("TRUNCATE TABLE games");
            preparedStatement.executeUpdate();
        } catch (SQLException error) {
            throw new DataAccessException("the server failed to clear the database");
        }
    }
}
