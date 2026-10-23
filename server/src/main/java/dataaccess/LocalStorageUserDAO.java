package dataaccess;

import model.AuthData;
import model.LoginData;
import model.UserData;

import java.util.HashMap;
import java.util.Map;

public class LocalStorageUserDAO implements UserDAO {
    private Map<String, UserData> userDataMap =  new HashMap<>();

    @Override
    public UserData getUserByUsername(String username) throws DataAccessException {
        if (username == null) {
            throw new DataAccessException("bad request");
        }
        return userDataMap.get(username);
    }

    @Override
    public AuthData registerUser(UserData request, LocalStorageAuthDAO auths) throws DataAccessException {
        String username = request.username();
        String password = request.password();
        String email = request.email();

        if ((username == null) || (password == null) || (email == null)) {
            throw new DataAccessException("bad request");
        }

        if (userDataMap.containsKey(username)) {
            throw new DataAccessException("already taken");
        }

        try {
            userDataMap.put(username, new UserData(username, password, email));
            return auths.createAuth(username);
        } catch (Exception error) {
            throw new DataAccessException("the server failed to register the user");
        }
    }

    public AuthData loginUser(LoginData request) throws DataAccessException {
        String username = request.username();
        String password = request.password();

        if ((username == null) || (password == null)) {
            throw new DataAccessException("bad request");
        }

        if (!(userDataMap.containsKey(username))
        || !(userDataMap.get(username).password().equals(password))) {
            throw new DataAccessException("unauthorized");
        }

        try {
            return new LocalStorageAuthDAO().createAuth(username);
        } catch (Exception error) {
            throw new DataAccessException("the server failed to login the user");
        }
    }

    @Override
    public void clear() throws DataAccessException {
        userDataMap.clear();
    }
}