package dataaccess;

import model.LoginData;
import model.UserData;

public class DatabaseUserDAO implements UserDAO {

    @Override
    public UserData getUserByUsername(String username) throws DataAccessException {
        return null;
    }

    @Override
    public void registerUser(UserData request) throws DataAccessException {

    }

    @Override
    public void loginUser(LoginData request) throws DataAccessException {

    }

    @Override
    public void clear() throws DataAccessException {

    }

    private void executeQuery(String query) throws ResponseException, DataAccessException {
        var connection = DatabaseManager.getConnection();
    }
}
