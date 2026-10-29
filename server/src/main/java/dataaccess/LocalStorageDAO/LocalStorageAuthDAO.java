package dataaccess.LocalStorageDAO;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import model.AuthData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LocalStorageAuthDAO implements AuthDAO {
    private Map<String, AuthData> auths = new HashMap<>();

    @Override
    public AuthData createAuth(String username) throws DataAccessException {
        if (username == null) {
            throw new DataAccessException("bad request");
        }

        try {
            String authToken = UUID.randomUUID().toString();
            AuthData newAuth = new AuthData(authToken, username);
            auths.put(authToken, newAuth);
            return newAuth;
        } catch (Exception error) {
            throw new DataAccessException("the server failed to create an authToken for the user");
        }

    }

    @Override
    public void logoutUser(String authToken) throws DataAccessException {
        if (!(auths.containsKey(authToken))) {
            throw new DataAccessException("unauthorized");
        }

        try {
            auths.remove(authToken);
        } catch (Exception error) {
            throw new DataAccessException("the server failed logout the user");
        }
    }

    public AuthData getAuth(String authToken) throws DataAccessException {
        if (!(auths.containsKey(authToken))) {
            throw new DataAccessException("unauthorized");
        }

        try {
            return auths.get(authToken);
        } catch (Exception error) {
            throw new DataAccessException("the server failed to process the authorization");
        }
    }

    public String getUsername(String authToken) throws DataAccessException {
        if (authToken == null) {
            throw new DataAccessException("bad request");
        }

        try {
            return auths.get(authToken).username();
        } catch (Exception error) {
            throw new DataAccessException("the server could not find the user");
        }
    }

    @Override
    public void clear() throws DataAccessException {
        auths.clear();
    }
}
