package dataaccess;

import model.UserData;
import server.RequestCreateUser;

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
    public void createUser(RequestCreateUser request) throws DataAccessException {
        String username = request.getUsername();
        String password = request.getPassword();
        String email = request.getEmail();

        if ((username == null) || (password == null) || (email == null)) {
            throw new DataAccessException("Bad request");
        }

        userDataMap.put(username, new UserData(username, password, email));
    }

    @Override
    public void deleteUser(String username) throws DataAccessException {
        if (username == null) {
            throw new DataAccessException("Bad request");
        }
        userDataMap.remove(username);
    }

    @Override
    public void clear() throws DataAccessException {
        userDataMap.clear();
    }
}