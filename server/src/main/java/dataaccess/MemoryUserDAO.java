package dataaccess;

import model.UserData;

import java.util.HashSet;

public class MemoryUserDAO implements UserDAO {
    @Override
    public UserData getUserByUsername(String username, HashSet<UserData> users) throws DataAccessException {
        if (username == null) {
            throw new DataAccessException("Username is null");
        }
        for(UserData user : users){
            if (user.username().equals(username)) {
                return user;
            }
        }
        throw new DataAccessException("Username not found");
    }

    @Override
    public void createUser(String username, String password, String email, HashSet<UserData> users) throws DataAccessException {
        if ((username == null) || (password == null) || (email == null)) {
            throw new DataAccessException("Username or password or email is null");
        }
        UserData newUser = new UserData(username, password, email);
        for (UserData user : users) {
            if (user.username().equals(newUser.username())) {
                throw new DataAccessException("Username already exists");
            }
        }
        users.add(newUser);
    }

    @Override
    public void deleteUser(String username, HashSet<UserData> users) throws DataAccessException {
        if (username == null) {
            throw new DataAccessException("Username is null");
        }
        UserData userToDelete = null;
        for(UserData user : users){
            if (user.username().equals(username)) {
                userToDelete = user;
            }
        }
        if (userToDelete != null) {
            users.remove(userToDelete);
        } else {
            throw new DataAccessException("Username not found");
        }
    }
}
