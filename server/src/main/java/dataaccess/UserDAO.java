package dataaccess;

import model.UserData;
import server.RequestCreateUser;

public interface UserDAO {
    UserData getUserByUsername(String username) throws DataAccessException;
    void createUser(RequestCreateUser request) throws DataAccessException;
    void clear() throws DataAccessException;
}
