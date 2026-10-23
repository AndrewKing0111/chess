package dataaccess;

import model.UserData;

public interface UserDAO {
    UserData getUserByUsername(String username) throws DataAccessException;
    void registerUser(UserData request) throws DataAccessException;
    void clear() throws DataAccessException;
}
