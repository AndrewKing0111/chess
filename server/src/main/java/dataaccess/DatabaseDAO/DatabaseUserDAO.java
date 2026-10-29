package dataaccess.DatabaseDAO;

import dataaccess.DataAccessException;
import dataaccess.ResponseException;
import dataaccess.UserDAO;
import model.LoginData;
import model.UserData;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseUserDAO implements UserDAO {

    public DatabaseUserDAO() throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String configStatement = """
                    CREATE TABLE IF NOT EXISTS users (
                      `username` varchar(256) NOT NULL,
                      `password` varchar(256) NOT NULL,
                      `email` varchar(256) NOT NULL,
                      PRIMARY KEY (`username`),
                      UNIQUE(username)
                    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
                    """;
            try (var preparedStatement = conn.prepareStatement(configStatement)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new DataAccessException("the server experienced an internal error");
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
