package dataaccess;

import dataaccess.sql.DatabaseAuthDAO;
import dataaccess.sql.DatabaseGameDAO;
import dataaccess.sql.DatabaseManager;
import dataaccess.sql.DatabaseUserDAO;

public class DatabaseDAOFactory implements DAOFactory {

    public DatabaseDAOFactory() throws DataAccessException {
        DatabaseManager.createDatabase();
    }

    public DatabaseAuthDAO createAuthDAO() throws DataAccessException {
        return new DatabaseAuthDAO();
    }

    public DatabaseGameDAO createGameDAO() throws DataAccessException {
        return new DatabaseGameDAO();
    }

    public DatabaseUserDAO createUserDAO() throws DataAccessException {
        return new DatabaseUserDAO();
    }
}
