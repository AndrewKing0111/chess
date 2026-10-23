package dataaccess;

import model.AuthData;
import model.LoginData;

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

        if (auths.containsKey(username)) {
            throw new DataAccessException("already taken");
        }

        try {
            auths.put(username, new AuthData(UUID.randomUUID().toString(), username));
            return auths.get(username);
        } catch (Exception error) {
            throw new DataAccessException("the server failed to create an authToken for the user");
        }

    }

    @Override
    public void logoutUser(String logoutRequest) throws DataAccessException {

    }

    @Override
    public void clear() throws DataAccessException {
        auths.clear();
    }
}
