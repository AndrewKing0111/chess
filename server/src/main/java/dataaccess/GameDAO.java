package dataaccess;

import model.GameData;
import model.RequestCreateGame;

import java.util.HashSet;

public interface GameDAO {
    int createGame(RequestCreateGame gameRequest) throws DataAccessException;

    void joinGame(int gameID, String playerColor) throws DataAccessException;

    HashSet<GameData> listGames(String authToken) throws DataAccessException;

    GameData getGame(int gameID) throws DataAccessException;

    void updateGame(int gameID, String gameString) throws DataAccessException;

    void clear() throws DataAccessException;
}
