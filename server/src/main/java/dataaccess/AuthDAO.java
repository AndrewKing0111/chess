package dataaccess;

import model.AuthData;

public interface AuthDAO {
    public AuthData createAuth(String username, String password, String email) throws DataAccessException;
    public AuthData getAuth(String username, String password) throws DataAccessException;
    public void deleteAuth(String username, String password) throws DataAccessException;
}
