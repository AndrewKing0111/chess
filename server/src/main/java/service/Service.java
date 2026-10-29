package service;

import dataaccess.*;
import dataaccess.DatabaseDAO.DatabaseAuthDAO;
import dataaccess.DatabaseDAO.DatabaseGameDAO;
import dataaccess.DatabaseDAO.DatabaseUserDAO;
import dataaccess.LocalStorageDAO.LocalStorageAuthDAO;
import dataaccess.LocalStorageDAO.LocalStorageGameDAO;
import dataaccess.LocalStorageDAO.LocalStorageUserDAO;
import model.*;


public class Service {

//    private final LocalStorageUserDAO users = new LocalStorageUserDAO();
//    private final LocalStorageGameDAO games = new LocalStorageGameDAO();
//    private final LocalStorageAuthDAO auths = new LocalStorageAuthDAO();

    private final DatabaseUserDAO users = new DatabaseUserDAO();
    private final DatabaseAuthDAO auths = new DatabaseAuthDAO();
    private final DatabaseGameDAO games = new DatabaseGameDAO();


    public Service() throws DataAccessException {}

    public void clear() throws DataAccessException {
        users.clear();
        games.clear();
        auths.clear();
    }

    public AuthData registerUser(UserData registerRequest) throws DataAccessException {
        users.registerUser(registerRequest);
        return auths.createAuth(registerRequest.username());
    }

    public AuthData loginUser(LoginData loginRequest) throws DataAccessException {
        users.loginUser(loginRequest);
        return auths.createAuth(loginRequest.username());
    }

    public void logoutUser(String authToken) throws DataAccessException {
        auths.logoutUser(authToken);
    }

    public int createGame(RequestCreateGame gameRequest) throws DataAccessException {
        auths.getAuth(gameRequest.authToken());
        return games.createGame(gameRequest);
    }

    public GameListData listGames(String authToken) throws DataAccessException {
        auths.getAuth(authToken);
        return games.listGames(authToken);
    }

    public void joinGame(RequestJoinGame joinRequest) throws DataAccessException {
        auths.getAuth(joinRequest.authToken());
        games.joinGame(joinRequest, auths.getUsername(joinRequest.authToken()));
    }
}
