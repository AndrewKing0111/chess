package dataaccess;

import model.AuthData;
import model.UserData;

public interface UserDAO {
    UserData getUserByUsername(String username) throws DataAccessException;
    AuthData registerUser(UserData request) throws DataAccessException;
    void clear() throws DataAccessException;
}
