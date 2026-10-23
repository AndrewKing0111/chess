package dataaccess;

import model.AuthData;

public interface AuthDAO {
    AuthData createAuth(String username) throws DataAccessException;
    AuthData getAuth(String username, String password) throws DataAccessException;
    void deleteAuth(String username, String password) throws DataAccessException;
    void clear() throws DataAccessException;
}
