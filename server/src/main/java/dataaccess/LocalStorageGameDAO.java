package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.RequestCreateGame;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class LocalStorageGameDAO implements GameDAO {
    private Map<Integer, GameData> gameList = new HashMap<>();
    private int counter = 0;

    @Override
    public void clear() throws DataAccessException {
        gameList.clear();
    }

    @Override
    public int createGame(RequestCreateGame gameRequest) throws DataAccessException {
        String authToken = gameRequest.authToken();
        String gameName = gameRequest.gameName();

        if ((authToken == null) || (gameName == null)) {
            throw new DataAccessException("bad request");
        }

        ChessGame chessGame = new ChessGame();
        GameData game = new GameData(counter, null, null, gameName, chessGame);
        try {
            gameList.put(counter, game);
            return counter++;
        } catch (Exception error) {
            throw new DataAccessException("the server failed to create the game");
        }
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
