package dataaccess;

import model.UserData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MemoryUserDAOTest {

    @Test
    void getUserByUsername() {
    }

    @Test
    void createUser() throws DataAccessException {
        assertTrue(true);
        var userDAO = new LocalStorageUserDAO();
        var user = userDAO.getUserByUsername("a");
        assertEquals(new UserData("ax", "1", "a"), user);
    }

    @Test
    void deleteUser() {
    }
}