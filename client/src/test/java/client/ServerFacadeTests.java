package client;

import org.junit.jupiter.api.*;
import facade.Server;

import static org.junit.jupiter.api.Assertions.fail;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade;
    private String authToken;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(8080);
        System.out.println("Started test HTTP server on " + port);
    }

    @BeforeEach
    public void setup() throws ResponseException {
        serverFacade = new ServerFacade("http://localhost:8080");
        serverFacade.clear();
        authToken = serverFacade.registerUser("u", "p", "e").authToken();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void createValidGame() {
        try {
            serverFacade.createGame("test", authToken);
        } catch (ResponseException e) {
            fail();
        }
    }

    @Test
    public void createGameInvalidAuthToken() {
        try {
            serverFacade.createGame("test", "bad auth token");
        } catch (ResponseException e) {
            fail();
        }
    }

}
