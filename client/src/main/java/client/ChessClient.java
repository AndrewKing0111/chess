package client;

import chess.ChessGame;
import server.ServerFacade;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static chess.ChessGame.TeamColor.*;
import static client.State.*;
import static ui.EscapeSequences.BLACK_KING;
import static ui.EscapeSequences.WHITE_KING;

public class ChessClient {
    private ServerFacade serverFacade;
    private State state = LOGGED_OUT;
    private ArrayList<String> authTokens = new ArrayList<>();
    private HashMap<ChessGame.TeamColor, Integer> games = new HashMap<>();

    public ChessClient() {
        try {
            waitForServer();
        } catch (Exception ex) {
            printException(ex);
        }

        try {
            serverFacade = new ServerFacade("http://localhost:8080");
        } catch (Exception ex) {
            printException(ex);
        }

        var scanner = new Scanner(System.in);
        String userInput;

        System.out.printf("%s Welcome to 240 Chess! %s\nType 'help' to get started.\n", WHITE_KING, BLACK_KING);
        while (true) {
            System.out.printf("[%s] >>> ", state);
            userInput = scanner.nextLine();
            String[] userInputParts = userInput.split(" ");

            if (state.equals(LOGGED_OUT)) {
                loggedOutMenu(userInputParts);
            } else {
                loggedInMenu(userInputParts);
            }
        }
    }

    private void loggedOutMenu(String[] parts) {
        try {
            switch (parts[0]) {
                case "help":
                    System.out.println("help - with possible commands");
                    System.out.println("login <USERNAME> <PASSWORD> - to play chess");
                    System.out.println("quit - playing chess");
                    System.out.println("register <USERNAME> <PASSWORD> <EMAIL> - to create an account");
                    break;
                case "login":
                    var result = serverFacade.loginUser(parts[1], parts[2]);
                    authTokens.add(result.authToken());
                    state = LOGGED_IN;
                    System.out.printf("Logged in as %s\n", result.username());
                    break;
                case "register":
                    result = serverFacade.registerUser(parts[1], parts[2], parts[3]);
                    authTokens.add(result.authToken());
                    state = LOGGED_IN;
                    System.out.printf("Logged in as %s\n", result.username());
                    break;
                case "quit":
                    System.out.println("Thanks for playing!");
                    System.exit(0);
                default:
                    System.out.println("Invalid input. Please try again.");
            }
        } catch (Exception ex) {
            printException(ex);
        }
    }

    private void loggedInMenu(String[] parts) {
        try {
            switch (parts[0]) {
                case "create":
                    serverFacade.createGame(parts[1], authTokens.getFirst());
                    break;
                case "help":
                    System.out.println("create <NAME> - a game");
                    System.out.println("help - with possible commands");
                    System.out.println("join [WHITE|BLACK] <ID> - a game");
                    System.out.println("list - games");
                    System.out.println("logout - when you are done");
                    System.out.println("observe <ID> - a game");
                    System.out.println("quit - playing chess");
                    break;
                case "list":
                    var gameList = serverFacade.listGames(authTokens.getFirst());
                    if (gameList.games().isEmpty()) {
                        System.out.println("No games created!");
                    } else {
                        System.out.println("Game ID  Game Name");
                        for (var game : gameList.games()) {
                            System.out.printf("%7s  %s\n", game.gameID(), game.gameName());
                        }
                    }
                    break;
                case "join":
                    serverFacade.joinGame(parts[1], parts[2]);
                    if (parts[1].equals("WHITE")) {
                        games.put(WHITE, Integer.valueOf(parts[2]));
                    } else {
                        games.put(BLACK, Integer.valueOf(parts[2]));
                    }
                    break;
                case "quit":
                    System.out.println("Thanks for playing!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid input. Please try again.");
            }
        } catch (Exception ex) {
            printException(ex);
        }
    }

    private void printException(Exception ex) {
        System.err.println(ex.getMessage());
        System.exit(1);
    }

    private static void waitForServer() throws InterruptedException {
        var address = new InetSocketAddress("localhost", 8080);
        long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(5);

        while (System.nanoTime() < deadline) {
            try (var socket = new Socket()) {
                socket.connect(address, 250);
                return;
            } catch (IOException e) {
                Thread.sleep(250);
            }
        }

    }
}
