package dataaccess.DatabaseDAO;

import dataaccess.DataAccessException;
import dataaccess.ResponseException;
import dataaccess.UserDAO;
import model.LoginData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

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
                      PRIMARY KEY (`username`)
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
            var preparedStatement = conn.prepareStatement("SELECT * FROM users WHERE username = ?");
            preparedStatement.setString(1, username);
            var result = preparedStatement.executeQuery();
            if (result.next()) {
                return new UserData(
                        result.getString("username"),
                        result.getString("password"),
                        result.getString("email")
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

        String hash = BCrypt.hashpw(password, BCrypt.gensalt());

        try (Connection conn = DatabaseManager.getConnection()) {
            var preparedStatement = conn.prepareStatement("INSERT INTO users VALUES (?, ?, ?)");
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, hash);
            preparedStatement.setString(3, email);
            preparedStatement.executeUpdate();
        } catch (SQLException error) {
            throw new DataAccessException("already taken");
        }
    }

    @Override
    public void loginUser(LoginData request) throws DataAccessException {
        String username = request.username();
        String password = request.password();

        if ((username == null) || (password == null)) {
            throw new DataAccessException("bad request");
        }

        try (Connection conn = DatabaseManager.getConnection()) {
            var preparedStatement = conn.prepareStatement("SELECT password FROM users WHERE username = ?");
            preparedStatement.setString(1, username);
            var result = preparedStatement.executeQuery();
            if (result.next()) {
                var userPassword = result.getString("password");
                if (!BCrypt.checkpw(password, userPassword)) {
                    throw new DataAccessException("unauthorized");
                }
            } else {
                throw new DataAccessException("unauthorized");
            }
        } catch (SQLException error) {
            throw new DataAccessException("unauthorized");
        }
    }

    @Override
    public void clear() throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            var preparedStatement = conn.prepareStatement("TRUNCATE TABLE users");
            preparedStatement.execute();
        } catch (Exception error) {
            throw new DataAccessException("the server failed to clear the database");
        }
    }
}
