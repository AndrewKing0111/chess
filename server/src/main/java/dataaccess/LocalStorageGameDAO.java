package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class LocalStorageGameDAO implements GameDAO {
    private Map<String, ChessGame> gameList = new HashMap<>();

    @Override
    public void clear() throws DataAccessException {
        gameList.clear();
    }

    @Override
    public int createGame(String gameName) throws DataAccessException {
        ChessGame game = new ChessGame();
        String gameID;
        if (gameList.isEmpty()) {
            gameID = "1";
        } else {
            gameID = Collections.max(gameList.keySet()) + 1;
        }
        gameList.put(gameID, game);
        return Integer.parseInt(gameID);
    }

    @Override
    public void joinGame(int gameID, String playerColor) throws DataAccessException {

    }

    @Override
    public HashSet<GameData> listGames(String authToken) throws DataAccessException {
        return null;
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return null;
    }

    @Override
    public void updateGame(int gameID, String gameString) throws DataAccessException {

    }
}
