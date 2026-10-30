package dataaccess;

import dataaccess.memory.MemoryAuthDAO;
import dataaccess.memory.MemoryGameDAO;
import dataaccess.memory.MemoryUserDAO;

public class MemoryDAOFactory implements DAOFactory {

    public MemoryAuthDAO createAuthDAO() {
        return new MemoryAuthDAO();
    }

    public GameDAO createGameDAO() {
        return new MemoryGameDAO();
    }

    public UserDAO createUserDAO() {
        return new MemoryUserDAO();
    }
}
