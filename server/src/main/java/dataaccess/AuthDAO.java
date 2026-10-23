package dataaccess;

import model.AuthData;
import model.LoginData;

public interface AuthDAO {
    AuthData createAuth(String username) throws DataAccessException;
    void getAuth(LoginData request) throws DataAccessException;
    void deleteAuth(String username, String password) throws DataAccessException;
    void clear() throws DataAccessException;
}
