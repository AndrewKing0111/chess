package dataaccess;

import model.AuthData;

public class MemoryAuthDAO implements AuthDAO {
    @Override
    public AuthData createAuth(String username, String password, String email) throws DataAccessException {
        return null;
    }

    @Override
    public AuthData getAuth(String username, String password) throws DataAccessException {
        return null;
    }

    @Override
    public void deleteAuth(String username, String password) throws DataAccessException {

    }
}
