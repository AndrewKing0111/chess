package dataaccess;

import dataaccess.database.DatabaseAuthDAO;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.fail;

public class DatabaseAuthDAOTests {

    private DatabaseAuthDAO databaseAuthDAO = new DatabaseAuthDAO();

    public DatabaseAuthDAOTests() throws DataAccessException {
    }

    @BeforeEach
    public void setup() {
        try {
            databaseAuthDAO.clear();
        } catch (DataAccessException error) {
            fail();
        }
    }
}
