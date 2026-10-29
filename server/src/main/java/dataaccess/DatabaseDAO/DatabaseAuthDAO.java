package dataaccess.DatabaseDAO;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import model.AuthData;

public class DatabaseAuthDAO implements AuthDAO {

    public DatabaseAuthDAO() throws DataAccessException {
        var db = new DatabaseManager();
        try {
            db.configureDatabase("""
            CREATE TABLE IF NOT EXISTS auths (
            `authToken` varchar(256) NOT NULL,
            `AuthData` TEXT NOT NULL,
            PRIMARY KEY (`authToken`),
            INDEX(authToken)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """);
        } catch (Exception error) {
            throw new DataAccessException("the server failed to initialize the database");
        }
    }

    @Override
    public AuthData createAuth(String username) throws DataAccessException {
        return null;
    }

    @Override
    public void logoutUser(String authToken) throws DataAccessException {

    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        return null;
    }

    @Override
    public String getUsername(String authToken) throws DataAccessException {
        return "";
    }

    @Override
    public void clear() throws DataAccessException {

    }
}
