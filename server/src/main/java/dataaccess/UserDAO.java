package dataaccess;

import model.UserData;

import java.util.HashSet;

public interface UserDAO {
    public UserData getUserByUsername(String username, HashSet<UserData> users) throws DataAccessException;
    public void createUser(String username, String password, String email, HashSet<UserData> users) throws DataAccessException;
    public void deleteUser(String username, HashSet<UserData> users) throws DataAccessException;
}
