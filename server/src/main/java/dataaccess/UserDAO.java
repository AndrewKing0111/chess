package dataaccess;

import model.AuthData;
import model.LoginData;
import model.UserData;

public interface UserDAO {
    UserData getUserByUsername(String username) throws DataAccessException;
    AuthData registerUser(UserData request) throws DataAccessException;
    AuthData loginUser(LoginData request) throws DataAccessException;
    void clear() throws DataAccessException;
}
