package dataaccess;

import model.AuthData;

public interface AuthDAO {
    AuthData createAuth(String username, String password, String email) throws DataAccessException;
    AuthData getAuth(String username, String password) throws DataAccessException;
    void deleteAuth(String username, String password) throws DataAccessException;
    void clear() throws DataAccessException;
}
