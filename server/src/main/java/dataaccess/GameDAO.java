package dataaccess;

import model.*;

public interface GameDAO {
    int createGame(RequestCreateGame gameRequest) throws DataAccessException;

    GameListData listGames(String authToken) throws DataAccessException;

    void joinGame(RequestJoinGame joinRequest, String username) throws DataAccessException;

    void clear() throws DataAccessException;
}
