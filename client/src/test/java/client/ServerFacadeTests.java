package client;

import model.AuthData;
import model.GameListData;
import org.junit.jupiter.api.*;
import facade.Server;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

    @Test
    public void joinGame() {
        try {
            createValidGame();
            serverFacade.joinGame("WHITE", "1");
        } catch (ResponseException e) {
            fail();
        }
    }

    @Test
    public void joinGameInvalidGameID() {
        try {
            createValidGame();
            serverFacade.joinGame("WHITE", "-1");
        } catch (ResponseException e) {
            fail();
        }
    }

    @Test
    public void listGames() {
        GameListData result = null;
        try {
            createValidGame();
            createValidGame();
            result = serverFacade.listGames(authToken);
        } catch (ResponseException e) {
            fail();
        }
        assertEquals(2, result.games().size());
    }

    @Test
    public void listGamesInvalidAuthToken() {
        try {
            createValidGame();
            createValidGame();
            serverFacade.listGames("bad auth token");
        } catch (ResponseException e) {
            assertEquals("Error: unauthorized", e.getMessage());
            return;
        }
        fail();
    }

    @Test
    public void loginUser() {
        AuthData result = null;
        try {
            result = serverFacade.loginUser("u", "p");
        } catch (ResponseException e) {
            fail();
        }
        assertEquals("u", result.username());
    }

    @Test
    public void loginUserInvalidPassword() {
        try {
            serverFacade.loginUser("u", "password");
        } catch (ResponseException e) {
            assertEquals("Error: unauthorized", e.getMessage());
            return;
        }
        fail();
    }
}
