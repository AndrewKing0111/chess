package services;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.LoginData;
import model.UserData;

public class Service {

    private UserDAO users;
    private GameDAO games;
    private AuthDAO auths;

    public Service(UserDAO users, GameDAO games, AuthDAO auths) {
        this.users = users;
        this.auths = auths;
        this.games = games;
    }

    public void clear() throws DataAccessException {
        users.clear();
        games.clear();
        auths.clear();
    }

    public AuthData registerUser(UserData registerRequest) throws DataAccessException {
        return users.registerUser(registerRequest);
    }

    public AuthData loginUser(LoginData loginRequest) throws DataAccessException {
        return users.loginUser(loginRequest);
    }

    public void logoutUser(String logoutRequest) throws DataAccessException {
        auths.logoutUser(logoutRequest);
    }
}
