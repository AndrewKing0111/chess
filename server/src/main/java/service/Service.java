package service;

import dataaccess.*;
import dataaccess.database.DatabaseAuthDAO;
import dataaccess.database.DatabaseGameDAO;
import dataaccess.database.DatabaseManager;
import dataaccess.database.DatabaseUserDAO;
import model.*;


public class Service {

    private final AuthDAO auths;
    private final GameDAO games;
    private final UserDAO users;


    public Service(DAOFactory factory) throws DataAccessException {
        this.auths = factory.createAuthDAO();
        this.games = factory.createGameDAO();
        this.users = factory.createUserDAO();
    }

    public void clear() throws DataAccessException {
        auths.clear();
        games.clear();
        users.clear();
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
