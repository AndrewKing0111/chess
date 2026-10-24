package dataaccess;

import chess.ChessGame;
import model.GameData;
import model.JoinGameData;
import model.RequestCreateGame;
import model.RequestJoinGame;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class LocalStorageGameDAO implements GameDAO {
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
            throw new DataAccessException("the server failed to create the game");
        }
    }

    @Override
    public HashSet<GameData> listGames(String authToken) throws DataAccessException {
        if ((authToken == null) || (gameList == null)) {
            throw new DataAccessException("bad request");
        }
        HashSet<GameData> gameSet = new HashSet<>(gameList.values());
        return gameSet;
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
                if (!(game.whiteUserName().isEmpty())) {
                    throw new DataAccessException("already taken");
                }
                counter--;
                createGame(new RequestCreateGame(authToken, game.gameName(), username, game.blackUserName()));
                return;
            case "BLACK":
                if (!(game.blackUserName().isEmpty())) {
                    throw new DataAccessException("already taken");
                }
                counter--;
                createGame(new RequestCreateGame(authToken, game.gameName(), game.whiteUserName(), username));
                return;
            default:
                throw new DataAccessException("bad request");
        }
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return gameList.get(gameID);
    }

    @Override
    public void updateGame(int gameID, String gameString) throws DataAccessException {

    }
}
