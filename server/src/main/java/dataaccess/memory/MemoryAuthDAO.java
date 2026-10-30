package dataaccess.memory;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import model.AuthData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO {
    private Map<String, AuthData> auths = new HashMap<>();

    @Override
    public AuthData createAuth(String username) throws DataAccessException {
        if (username == null) {
            throw new DataAccessException("bad request");
        }

        String authToken = UUID.randomUUID().toString();
        AuthData newAuth = new AuthData(authToken, username);

        try {
            auths.put(authToken, newAuth);

        } catch (Exception error) {
            throw new DataAccessException("the server experienced an internal error");
        }
        return newAuth;
    }

    @Override
    public void logoutUser(String authToken) throws DataAccessException {
        if (authToken == null) {
            throw new DataAccessException("bad request");
        }

        if (!(auths.containsKey(authToken))) {
            throw new DataAccessException("unauthorized");
        }

        try {
            auths.remove(authToken);
        } catch (Exception error) {
            throw new DataAccessException("the server experienced an internal error");
        }
    }

    public AuthData getAuth(String authToken) throws DataAccessException {
        if (authToken == null) {
            throw new DataAccessException("bad request");
        }

        if (!(auths.containsKey(authToken))) {
            throw new DataAccessException("unauthorized");
        }

        try {
            return auths.get(authToken);
        } catch (Exception error) {
            throw new DataAccessException("the server experienced an internal error");
        }
    }

    public String getUsername(String authToken) throws DataAccessException {
        if (authToken == null) {
            throw new DataAccessException("bad request");
        }

        try {
            return auths.get(authToken).username();
        } catch (Exception error) {
            throw new DataAccessException("the server experienced an internal error");
        }
    }

    @Override
    public void clear() throws DataAccessException {
        auths.clear();
    }
}
