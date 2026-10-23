package services;

import dataaccess.*;
import model.AuthData;
import model.LoginData;
import model.UserData;

public class Service {

    private LocalStorageUserDAO users = new LocalStorageUserDAO();
    private LocalStorageGameDAO games = new LocalStorageGameDAO();
    private LocalStorageAuthDAO auths = new LocalStorageAuthDAO();

    public Service() {}

    public void clear() throws DataAccessException {
        users.clear();
        games.clear();
        auths.clear();
    }

    public AuthData registerUser(UserData registerRequest) throws DataAccessException {
        return users.registerUser(registerRequest, auths);
    }

    public AuthData loginUser(LoginData loginRequest) throws DataAccessException {
        return users.loginUser(loginRequest);
    }

    public void logoutUser(String authToken) throws DataAccessException {
        auths.logoutUser(authToken);
    }
}
