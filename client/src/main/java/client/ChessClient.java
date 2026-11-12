package client;

import server.ServerFacade;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashSet;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import static client.State.*;
import static ui.EscapeSequences.BLACK_KING;
import static ui.EscapeSequences.WHITE_KING;

public class ChessClient {
    private ServerFacade serverFacade;
    private State state = SIGNED_OUT;
    private HashSet<String> authTokens = new HashSet<>();

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
        while(true) {
            System.out.printf("[%s] >>> ", state);
            userInput = scanner.nextLine();
            String[] userInputParts = userInput.split(" ");

            if (state.equals(SIGNED_OUT)) {
                signedOutMenu(userInputParts);
            } else {
                signedInMenu(userInputParts);
            }
        }
    }

    private void signedOutMenu(String[] parts) {
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
                    state = SIGNED_IN;
                    break;
                case "register":
                    result = serverFacade.registerUser(parts[1], parts[2], parts[3]);
                    authTokens.add(result.authToken());
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

    private void signedInMenu(String[] parts) {

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
