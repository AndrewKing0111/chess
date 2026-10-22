package dataaccess;

import model.UserData;

public interface UserDAO {
    UserData getUserByUsername(String username) throws DataAccessException;
    void createUser(String username, String password, String email) throws DataAccessException;
    void deleteUser(String username) throws DataAccessException;
    void clear() throws DataAccessException;
}
