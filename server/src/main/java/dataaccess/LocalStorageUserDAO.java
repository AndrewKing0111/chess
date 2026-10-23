package dataaccess;

import model.AuthData;
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
    public AuthData registerUser(UserData request) throws DataAccessException {
        String username = request.username();
        String password = request.password();
        String email = request.email();

        if ((username == null) || (password == null) || (email == null)) {
            throw new DataAccessException("Bad request.");
        }

        if (userDataMap.containsKey(username)) {
            throw new DataAccessException("Already taken.");
        }

        try {
            userDataMap.put(username, new UserData(username, password, email));
            var authDAO = new LocalStorageAuthDAO();
            var auth = authDAO.createAuth(username);
            return auth;
        } catch (Exception error) {
            throw new DataAccessException("The server failed to register the user.");
        }
    }

    @Override
    public void clear() throws DataAccessException {
        userDataMap.clear();
    }
}