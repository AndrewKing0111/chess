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
import java.util.HashSet;

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
                      `games` TEXT NOT NULL,
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
                    "gameName, games) VALUES (?, ?, ?, ?, ?)");
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
        try (Connection conn = DatabaseManager.getConnection()) {
            var preparedStatement = conn.prepareStatement("SELECT * FROM games");
            var result = preparedStatement.executeQuery();
            var gameList = new HashSet<GameData>();
            boolean flag = false;
            while (result.next()) {
                int gameID = result.getInt("gameID");
                String whiteUsername = result.getString("whiteUsername");
                String blackUsername = result.getString("blackUsername");
                String gameName = result.getString("gameName");

                ChessGame chessGame = new Gson().fromJson(result.getString("games"), ChessGame.class);

                GameData game = new GameData(
                        gameID,
                        whiteUsername,
                        blackUsername,
                        gameName,
                        chessGame
                );

                gameList.add(game);
                flag = true;
            }
            if (flag) {
                return new GameListData(gameList);
            }
            return new GameListData(new HashSet<>());
        } catch (SQLException error) {
            throw new DataAccessException("the server experienced an internal error");
        }
    }

    @Override
    public void joinGame(RequestJoinGame joinRequest, String username) throws DataAccessException {
        String authToken = joinRequest.authToken();
        int gameID = joinRequest.gameID();
        String playerColor = joinRequest.playerColor();

        if ((gameID < 1) || (playerColor == null)) {
            throw new DataAccessException("bad request");
        }

        try (Connection conn = DatabaseManager.getConnection()) {
            var preparedStatement = conn.prepareStatement("SELECT GameData FROM games WHERE GameID = ?");
            preparedStatement.setInt(1, gameID);
            var result = preparedStatement.executeQuery();
            if (result.next()) {
                GameData game = new Gson().fromJson(result.getString("GameData"), GameData.class);
                switch (playerColor) {
                    case "WHITE":
                        if (game.whiteUsername() != null) {
                            throw new DataAccessException("already taken");
                        }
                        counter--;
                        createGame(new RequestCreateGame(authToken, game.gameName(), username, game.blackUsername()));
                        return;
                    case "BLACK":
                        if (game.blackUsername() != null) {
                            throw new DataAccessException("already taken");
                        }
                        counter--;
                        createGame(new RequestCreateGame(authToken, game.gameName(), game.whiteUsername(), username));
                        return;
                    default:
                        throw new DataAccessException("bad request");
                }
            } else {
                throw new DataAccessException("bad request");
            }
        } catch (SQLException error) {
            throw new DataAccessException("the server experienced an internal error");
        }
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
