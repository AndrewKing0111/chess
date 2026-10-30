package dataaccess;

public interface DAOFactory {
    AuthDAO createAuthDAO() throws DataAccessException;

    GameDAO createGameDAO() throws DataAccessException;

    UserDAO createUserDAO() throws DataAccessException;
}
