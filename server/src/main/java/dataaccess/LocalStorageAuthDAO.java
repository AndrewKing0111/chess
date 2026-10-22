package dataaccess;

import model.AuthData;

import java.util.HashMap;
import java.util.Map;

public class LocalStorageAuthDAO implements AuthDAO {
    private Map<String, AuthDAO> auths = new HashMap<>();

    @Override
    public AuthData createAuth(String username, String password, String email) throws DataAccessException {
        return null;
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
