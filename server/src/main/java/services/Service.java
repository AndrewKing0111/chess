package services;

import dataaccess.*;
import model.*;

import java.util.HashMap;
import java.util.HashSet;

public class Service {

    private LocalStorageUserDAO users = new LocalStorageUserDAO();
    private LocalStorageGameDAO games = new LocalStorageGameDAO();
    private LocalStorageAuthDAO auths = new LocalStorageAuthDAO();

    public Service() {
    }

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

    public HashSet<GameData> listGames(String authToken) throws DataAccessException {
        auths.getAuth(authToken);
        return games.listGames(authToken);
    }

    public void joinGame(RequestJoinGame joinRequest) throws DataAccessException {
        auths.getAuth(joinRequest.authToken());
        String username = auths.getUsername(joinRequest.authToken());
        games.joinGame(joinRequest, username);
    }
}
