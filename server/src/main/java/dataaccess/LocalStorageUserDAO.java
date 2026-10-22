package dataaccess;

import model.UserData;

import java.util.HashMap;
import java.util.Map;

public class LocalStorageUserDAO implements UserDAO {
    private Map<String, UserData> userDataMap =  new HashMap<>();

    @Override
    public UserData getUserByUsername(String username) throws DataAccessException {
        if (username == null) {
            throw new DataAccessException("Username is null");
        }
        return userDataMap.get(username);
    }

    @Override
    public void createUser(String username, String password, String email) throws DataAccessException {
        if ((username == null) || (password == null) || (email == null)) {
            throw new DataAccessException("Username or password or email is null");
        }
        userDataMap.put(username, new UserData(username, password, email));
    }

    @Override
    public void deleteUser(String username) throws DataAccessException {
        if (username == null) {
            throw new DataAccessException("Username is null");
        }
        userDataMap.remove(username);
    }

    @Override
    public void clear() throws DataAccessException {
        userDataMap.clear();
    }
}