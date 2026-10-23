package dataaccess;

import model.AuthData;

public interface AuthDAO {
    AuthData createAuth(String username) throws DataAccessException;
    void logoutUser(String authToken) throws DataAccessException;
    void clear() throws DataAccessException;
}
