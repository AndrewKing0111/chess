package dataaccess;

import dataaccess.database.DatabaseAuthDAO;
import dataaccess.database.DatabaseGameDAO;
import dataaccess.database.DatabaseUserDAO;

public class DatabaseDAOFactory implements DAOFactory {

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
