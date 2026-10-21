package dataaccess;

import model.AuthData;
import model.GameData;

import java.util.HashSet;

public interface GameDAO {
    public void clear() throws DataAccessException;
    public int createGame(String gameName) throws DataAccessException;
    public void joinGame(int gameID, String playerColor) throws DataAccessException;
    public HashSet<GameData> listGames(String authToken) throws DataAccessException;
    public GameData getGame(int gameID) throws DataAccessException;
    public void updateGame(int gameID, String gameString) throws DataAccessException;
}
