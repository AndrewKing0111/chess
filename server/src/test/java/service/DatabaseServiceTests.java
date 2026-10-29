package service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

public class DatabaseServiceTests {

    private static Service service;

    @BeforeEach
    public void setup() {
        try {
            service = new Service();
        } catch (Exception error) {
            fail();
        }
    }

    @Test
    void clear() {
        try {
            service.clear();
        } catch (Exception error) {
            fail();
        }
    }
}
