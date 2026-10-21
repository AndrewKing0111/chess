package dataaccess;

import model.AuthData;

public interface UserDAO {
    public AuthData getUserByUsername(String username) throws DataAccessException;
    public void createUser(String username, String password, String email) throws DataAccessException;
    public void deleteUser(String username) throws DataAccessException;
}
