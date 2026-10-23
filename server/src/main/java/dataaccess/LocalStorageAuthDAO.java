package dataaccess;

import kotlin.TuplesKt;
import model.AuthData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LocalStorageAuthDAO implements AuthDAO {
    private Map<String, AuthData> auths = new HashMap<>();

    @Override
    public AuthData createAuth(String username) throws DataAccessException {
        if (username == null) {
            throw new DataAccessException("Bad request.");
        }

        if (auths.containsKey(username)) {
            throw new DataAccessException("Already taken.");
        }

        try {
            auths.put(username, new AuthData(UUID.randomUUID().toString(), username));
            return auths.get(username);
        } catch (Exception error) {
            throw new DataAccessException("The server failed to create an authToken for the user.");
        }

    }

    @Override
    public AuthData getAuth(String username, String password) throws DataAccessException {
        return null;
    }

    @Override
    public void deleteAuth(String username, String password) throws DataAccessException {

    }

    @Override
    public void clear() throws DataAccessException {
        auths.clear();
    }
}
