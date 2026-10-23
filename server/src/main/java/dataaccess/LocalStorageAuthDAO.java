package dataaccess;

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

        if (auths.containsKey(username)) {
            auths.remove(username);
        }

        try {
            auths.put(username, new AuthData(UUID.randomUUID().toString(), username));
            return auths.get(username);
        } catch (Exception error) {
            throw new DataAccessException("the server failed to create an authToken for the user");
        }

    }

    @Override
    public void logoutUser(String authToken) throws DataAccessException {
        String username = "";
        for (Map.Entry<String, AuthData> entry : auths.entrySet()) {
            if (entry.getValue().authToken().equals(authToken)) {
                username = entry.getKey();
            }
        }
        if (username.isEmpty()) {
            throw new DataAccessException("unauthorized");
        } else {
            try {
                auths.remove(username);
            } catch (Exception error) {
                throw new DataAccessException("the server failed logout the user");
            }
        }
    }

    public AuthData getAuth(String authToken) throws DataAccessException {
        for (Map.Entry<String, AuthData> entry : auths.entrySet()) {
            if (entry.getValue().authToken().equals(authToken)) {
                return entry.getValue();
            }
        }
        throw new DataAccessException("unauthorized");
    }

    @Override
    public void clear() throws DataAccessException {
        auths.clear();
    }
}
