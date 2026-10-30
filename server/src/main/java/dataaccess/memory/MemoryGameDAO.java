package dataaccess.memory;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class MemoryGameDAO implements GameDAO {
    private Map<Integer, GameData> gameList = new HashMap<>();
    private int counter = 1;

    @Override
    public void clear() throws DataAccessException {
        gameList.clear();
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

        ChessGame chessGame = new ChessGame();
        GameData game = new GameData(counter, whiteUserName, blackUserName, gameName, chessGame);
        try {
            gameList.put(counter, game);
            return counter++;
        } catch (Exception error) {
            throw new DataAccessException("the server experienced an internal error");
        }
    }

    @Override
    public GameListData listGames(String authToken) throws DataAccessException {
        if ((authToken == null) || (gameList == null)) {
            throw new DataAccessException("bad request");
        }
        return new GameListData(new HashSet<>(gameList.values()));
    }

    @Override
    public void joinGame(RequestJoinGame joinRequest, String username) throws DataAccessException {
        String authToken = joinRequest.authToken();
        int gameID = joinRequest.gameID();
        String playerColor = joinRequest.playerColor();

        if ((gameID < 1) || (playerColor == null)) {
            throw new DataAccessException("bad request");
        }

        GameData game = gameList.get(gameID);
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
    }
}
