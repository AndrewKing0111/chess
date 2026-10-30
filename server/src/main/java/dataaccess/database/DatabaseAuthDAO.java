package dataaccess.database;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import model.AuthData;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;

public class DatabaseAuthDAO implements AuthDAO {

    public DatabaseAuthDAO() throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String configStatement = """
                    CREATE TABLE IF NOT EXISTS auths (
                      `authToken` varchar(256) NOT NULL,
                      `AuthData` TEXT NOT NULL,
                      PRIMARY KEY (`authToken`),
                      INDEX(authToken)
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
    public AuthData createAuth(String username) throws DataAccessException {
        if (username == null) {
            throw new DataAccessException("bad request");
        }

        String authToken = UUID.randomUUID().toString();
        AuthData newAuth = new AuthData(authToken, username);
        var newAuthJSON = new Gson().toJson(newAuth);

        try (Connection conn = DatabaseManager.getConnection()) {
            var preparedStatement = conn.prepareStatement("INSERT INTO auths VALUES (?, ?)");
            preparedStatement.setString(1, authToken);
            preparedStatement.setString(2, newAuthJSON);
            preparedStatement.executeUpdate();
        } catch (SQLException error) {
            throw new DataAccessException("the server experienced an internal error");
        }
        return newAuth;
    }

    @Override
    public void logoutUser(String authToken) throws DataAccessException {
        if (authToken == null) {
            throw new DataAccessException("bad request");
        }

        try (Connection conn = DatabaseManager.getConnection()) {
            var preparedStatement = conn.prepareStatement("DELETE FROM auths WHERE authToken = ?");
            preparedStatement.setString(1, authToken);
            var result = preparedStatement.executeUpdate();
            if (result < 1) {
                throw new DataAccessException("unauthorized");
            }
        } catch (SQLException error) {
            throw new DataAccessException("the server experienced an internal error");
        }
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        if (authToken == null) {
            throw new DataAccessException("bad request");
        }

        try (Connection conn = DatabaseManager.getConnection()) {
            var preparedStatement = conn.prepareStatement("SELECT AuthData FROM auths WHERE authToken = ?");
            preparedStatement.setString(1, authToken);
            var result = preparedStatement.executeQuery();
            if (result.next()) {
                return new Gson().fromJson(result.getString("AuthData"), AuthData.class);
            } else {
                throw new DataAccessException("unauthorized");
            }
        } catch (SQLException error) {
            throw new DataAccessException("the server experienced an internal error");
        }
    }

    @Override
    public String getUsername(String authToken) throws DataAccessException {
        if (authToken == null) {
            throw new DataAccessException("bad request");
        }

        try (Connection conn = DatabaseManager.getConnection()) {
            var preparedStatement = conn.prepareStatement("SELECT AuthData FROM auths WHERE authToken = ?");
            preparedStatement.setString(1, authToken);
            var result = preparedStatement.executeQuery();
            if (result.next()) {
                var auth = new Gson().fromJson(result.getString("AuthData"), AuthData.class);
                return auth.username();
            } else {
                throw new DataAccessException("unauthorized");
            }
        } catch (SQLException error) {
            throw new DataAccessException("the server experienced an internal error");
        }
    }

    @Override
    public void clear() throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            var preparedStatement = conn.prepareStatement("TRUNCATE TABLE auths");
            preparedStatement.executeUpdate();
        } catch (SQLException error) {
            throw new DataAccessException("the server experienced an internal error");
        }
    }
}
