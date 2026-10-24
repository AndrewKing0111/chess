package dataaccess;

import model.GameData;
import model.JoinGameData;
import model.RequestCreateGame;
import model.RequestJoinGame;

import java.util.HashSet;

public interface GameDAO {
    int createGame(RequestCreateGame gameRequest) throws DataAccessException;
    HashSet<GameData> listGames(String authToken) throws DataAccessException;
    void joinGame(RequestJoinGame joinRequest, String username) throws DataAccessException;
    GameData getGame(int gameID) throws DataAccessException;
    void updateGame(int gameID, String gameString) throws DataAccessException;
    void clear() throws DataAccessException;
}
