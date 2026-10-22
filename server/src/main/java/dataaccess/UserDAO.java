package dataaccess;

import model.UserData;

import java.util.HashSet;

public interface UserDAO {
    public UserData getUserByUsername(String username) throws DataAccessException;
    public void createUser(String username, String password, String email) throws DataAccessException;
    public void deleteUser(String username) throws DataAccessException;
    void clear() throws DataAccessException;
}
