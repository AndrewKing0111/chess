package dataaccess;

import model.AuthData;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

public class LocalStorageAuthDAO implements AuthDAO {
    private HashSet<AuthData> auths = new HashSet<>();

    @Override
    public AuthData createAuth(String username) throws DataAccessException {
        if (username == null) {
            throw new DataAccessException("bad request");
        }

        try {
            AuthData newAuth = new AuthData(UUID.randomUUID().toString(), username);
            auths.add(newAuth);
            return newAuth;
        } catch (Exception error) {
            throw new DataAccessException("the server failed to create an authToken for the user");
        }

    }

    @Override
    public void logoutUser(String authToken) throws DataAccessException {
        String username = "";
        AuthData authToRemove = new AuthData("", "");
        for (AuthData auth : auths) {
            if (auth.authToken().equals(authToken)) {
                username = auth.username();
                authToRemove = auth;
            }
        }
        if (username.isEmpty()) {
            throw new DataAccessException("unauthorized");
        } else {
            try {
                auths.remove(authToRemove);
            } catch (Exception error) {
                throw new DataAccessException("the server failed logout the user");
            }
        }
    }

    public AuthData getAuth(String authToken) throws DataAccessException {
        for (AuthData auth : auths) {
            if (auth.authToken().equals(authToken)) {
                return auth;
            }
        }
        throw new DataAccessException("unauthorized");
    }

    public String getUsername(String authToken) throws DataAccessException {
        if (authToken == null) {
            throw new DataAccessException("bad request");
        }

        for (AuthData auth : auths) {
            if (auth.authToken().equals(authToken)) {
                return auth.username();
            }
        }
        throw new DataAccessException("the server could not find the user");
    }

    @Override
    public void clear() throws DataAccessException {
        auths.clear();
    }
}
