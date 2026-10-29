package dataaccess.DatabaseDAO;

import dataaccess.DataAccessException;
import dataaccess.ResponseException;
import dataaccess.UserDAO;
import model.LoginData;
import model.UserData;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

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
        if (username == null) {
            throw new DataAccessException("bad request");
        }

        try (Connection conn = DatabaseManager.getConnection()) {
            var preparedStatement = conn.prepareStatement("SELECT * FROM users WHERE username = ?", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, username);
            var results = preparedStatement.executeQuery();
            if (results.next()) {
                return new UserData(
                        results.getString("username"),
                        results.getString("password"),
                        results.getString("email")
                );
            } else {
                throw new DataAccessException("bad request");
            }
        } catch (SQLException error) {
            throw new DataAccessException("the server failed to access the database");
        }
    }

    @Override
    public void registerUser(UserData request) throws DataAccessException {
        String username = request.username();
        String password = request.password();
        String email = request.email();

        if ((username == null) || (password == null) || (email == null)) {
            throw new DataAccessException("bad request");
        }

        try (Connection conn = DatabaseManager.getConnection()) {
            var preparedStatement = conn.prepareStatement("INSERT INTO users VALUES (?, ?, ?)");
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, email);
        } catch (SQLException error) {
            if (error.getMessage().contains("Duplicate")) {
                throw new DataAccessException("already taken");
            } else {
                throw new DataAccessException("the server failed to register the user");
            }
        }
    }

    @Override
    public void loginUser(LoginData request) throws DataAccessException {

    }

    @Override
    public void clear() throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            var preparedStatement = conn.prepareStatement("DROP ALL TABLES");
            preparedStatement.execute();

            var resultSet = preparedStatement.getGeneratedKeys();

        } catch (Exception error) {
            throw new DataAccessException("the server failed to clear the database");
        }
    }

    private void executeQuery(String query) throws ResponseException, DataAccessException {
        var connection = DatabaseManager.getConnection();
    }
}
