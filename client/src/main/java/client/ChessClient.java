package client;

import java.util.Scanner;

import static client.State.*;
import static ui.EscapeSequences.BLACK_KING;
import static ui.EscapeSequences.WHITE_KING;

public class ChessClient {
    private State state = SIGNED_OUT;

    public ChessClient() {
        var scanner = new Scanner(System.in);
        String userInput;

        System.out.printf("%s Welcome to 240 Chess! %s\nType 'help' to get started.\n", WHITE_KING, BLACK_KING);
        while(true) {
            System.out.printf("[%s] >>> ", state);
            userInput = scanner.nextLine();
            String[] parts = userInput.split(" ");
            switch (parts[0]) {
                case "help":
                    System.out.println("help - with possible commands");
                    System.out.println("login <USERNAME> <PASSWORD> - to play chess");
                    System.out.println("quit - playing chess");
                    System.out.println("register <USERNAME> <PASSWORD> <EMAIL> - to create an account");
                    break;
                case "login":
                    postLogin(parts[1], parts[2]);
                    break;
                default:
                    System.out.println("Invalid input. Please try again.");
            }
        }
    }

    private void postLogin(String username, String password) {

    }
}
