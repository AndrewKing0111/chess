package dataaccess.DatabaseDAO;

import dataaccess.DataAccessException;
import dataaccess.ResponseException;
import dataaccess.UserDAO;
import model.LoginData;
import model.UserData;

public class DatabaseUserDAO implements UserDAO {

    public DatabaseUserDAO() throws DataAccessException {
        var db = new DatabaseManager();
        try {
            db.configureDatabase("""
            CREATE TABLE IF NOT EXISTS users (
              `username` varchar(256) NOT NULL,
              `password` varchar(256) NOT NULL,
              `email` varchar(256) NOT NULL,
              PRIMARY KEY (`username`),
              UNIQUE(username)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """);
        } catch (Exception error) {
            throw new DataAccessException("the server failed to initialize the database");
        }
    }

    @Override
    public UserData getUserByUsername(String username) throws DataAccessException {
        return null;
    }

    @Override
    public void registerUser(UserData request) throws DataAccessException {

    }

    @Override
    public void loginUser(LoginData request) throws DataAccessException {

    }

    @Override
    public void clear() throws DataAccessException {

    }

    private void executeQuery(String query) throws ResponseException, DataAccessException {
        var connection = DatabaseManager.getConnection();
    }
}
