package dataAccess;

import model.*;

public interface GameDAO {
    int createGame(RequestCreateGame gameRequest) throws DataAccessException;
    GameListData listGames(String authToken) throws DataAccessException;
    void joinGame(RequestJoinGame joinRequest, String username) throws DataAccessException;
    GameData getGame(int gameID) throws DataAccessException;
    void updateGame(int gameID, String gameString) throws DataAccessException;
    void clear() throws DataAccessException;
}
