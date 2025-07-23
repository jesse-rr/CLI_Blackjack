package code;

import code.models.Player;

import java.io.*;
import java.util.*;

public class Startup {
    private static final int SLEEP_SHORT = 1000; // 1 second

    public static void main(String[] args) {
        displayFirstMenu();
    }

    private static void displayFirstMenu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            MenuManagement.showUserAccessMenu();
            String choice = scanner.next().trim().toLowerCase();

            try {
                switch (choice) {
                    case "s": signUp(); break;
                    case "l": login(); break;
                    case "g": playAsGuest(); break;
                    case "x": System.exit(0); break;
                    default: showInvalidChoice();
                }
            } catch (IOException | InterruptedException e) {
                System.err.println("An error occurred: " + e.getMessage());
            }
        }
    }

    private static void login() throws IOException, InterruptedException {
        Scanner userInput = new Scanner(System.in);
        String username;
        String password;

        while (true) {
            System.out.print("Enter username: ");
            username = userInput.next().trim();
            System.out.print("Enter password: ");
            password = userInput.next().trim();

            if (username.contains(" ") || username.isEmpty() || password.contains(" ") || password.isEmpty() || password.length() <= 5) {
                System.out.println("Username/password cannot have spaces, be blank, or have a password less than 6 characters.");
                Thread.sleep(SLEEP_SHORT);
                continue;
            }

            try {
                Player loggedInPlayer = SaveManagement.findPlayer(username, password);

                if (loggedInPlayer != null) {
                    System.out.println("Login successful!");
                    Thread.sleep(SLEEP_SHORT);
                    mainMenu(loggedInPlayer);
                    return;
                } else {
                    System.out.println("Login failed. Invalid username or password.");
                    Thread.sleep(SLEEP_SHORT);
                    return;
                }
            } catch (IOException e) {
                System.out.println("Error accessing user data. Please try again.");
                Thread.sleep(SLEEP_SHORT);
                return;
            }
        }
    }

    private static void signUp() throws IOException, InterruptedException {
        Scanner userInput = new Scanner(System.in);
        String username;
        String password;

        while (true) {
            System.out.print("Username: ");
            username = userInput.next().trim();
            System.out.print("Password: ");
            password = userInput.next().trim();

            if (username.contains(" ") || username.isEmpty() || password.contains(" ") || password.isEmpty() || password.length() <= 5) {
                System.out.println("Username/password cannot have spaces, be blank, or have a password less than 6 characters.");
                Thread.sleep(SLEEP_SHORT);
            } else if (username.equalsIgnoreCase("guest")) {
                System.out.println("Username cannot be 'guest'.");
                Thread.sleep(SLEEP_SHORT);
            } else {
                break;
            }
        }

        if (SaveManagement.playerExists(username)) {
            System.out.println("Username already exists. Please choose a different username.");
            Thread.sleep(SLEEP_SHORT);
            return;
        }

        Player newPlayer = new Player(username, password, 2000.0, 0.0, 0, 0);
        SaveManagement.createPlayer(newPlayer);

        System.out.println("User signed up successfully!");
        Thread.sleep(SLEEP_SHORT);
        login();
    }

    private static void playAsGuest() throws IOException, InterruptedException {
        Player guestPlayer = new Player("Guest", "", 1000.0, 0.0, 0, 0);
        System.out.println("Playing as Guest.");
        Thread.sleep(SLEEP_SHORT);
        mainMenu(guestPlayer);
    }

    private static void mainMenu(Player player) throws IOException, InterruptedException {
        Scanner userInput = new Scanner(System.in);
        boolean exitMenu = false;

        while (!exitMenu) {
            MenuManagement.showMainMenu();
            String menuChoice = userInput.next().trim().toLowerCase();

            switch (menuChoice) {
                case "s": startGame(player);
                    break;
                case "a": account(player);
                    break;
                case "x":
                    exitMenu = true;
                    if (!player.getName().equalsIgnoreCase("guest")) {
                        SaveManagement.savePlayerInfo(player);
                        System.out.println("Player data saved.");
                    }
                    System.out.println("Exiting game. Goodbye!");
                    Thread.sleep(SLEEP_SHORT);
                    break;
                case "h": helpMenu();
                    break;
                default: showInvalidChoice();
            }
        }
    }

    private static void helpMenu() throws InterruptedException {
        Scanner userInput = new Scanner(System.in);
        boolean exitHelp = false;
        while (!exitHelp) {
            MenuManagement.showHelpMenu();
            String menuChoice = userInput.next().trim().toLowerCase();
            if (menuChoice.equals("x")) {
                exitHelp = true;
            } else {
                showInvalidChoice();
            }
        }
    }

    private static void account(Player player) throws InterruptedException {
        Scanner userInput = new Scanner(System.in);
        boolean exitAccount = false;
        while (!exitAccount) {
            MenuManagement.showAccountInfoMenu(
                    player.getName(),
                    player.getBalance(),
                    player.getWinRate(),
                    player.getWins(),
                    player.getLoses()
            );
            String menuChoice = userInput.next().trim().toLowerCase();
            if (menuChoice.equals("x")) {
                exitAccount = true;
            } else {
                showInvalidChoice();
            }
        }
    }

    private static void startGame(Player player) throws IOException, InterruptedException {
        if (player.getBalance() <= 0) {
            System.out.println("Sorry, your balance is R$0.00. You can't play right now.");
            Thread.sleep(SLEEP_SHORT * 2);
            if (!player.getName().equalsIgnoreCase("guest")) {
                System.out.println("As a loyal player, we'll give you R$500.00 to continue playing!");
                player.setBalance(500.00);
                SaveManagement.savePlayerInfo(player);
                Thread.sleep(SLEEP_SHORT * 2);
            } else {
                System.out.println("Guest accounts cannot receive free balance. Please sign up to continue playing.");
                Thread.sleep(SLEEP_SHORT * 2);
                return;
            }
        }

        double bet = 0;
        Scanner userInput = new Scanner(System.in);

        while (true) {
            MenuManagement.showInitialBetMenu(player.getBalance());
            System.out.print("Enter your bet amount: R$ ");
            String input = userInput.next().trim().replace(',', '.');
            try {
                bet = Double.parseDouble(input);
                if (bet <= 0 || bet > player.getBalance()) {
                    System.out.println("Invalid bet amount. Please enter a positive amount within your balance.");
                    Thread.sleep(SLEEP_SHORT);
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a valid number for your bet.");
                Thread.sleep(SLEEP_SHORT);
            }
        }

        player.setBalance(player.getBalance() - bet);
        GameActions.table(player, bet);
    }

    private static void showInvalidChoice() throws InterruptedException {
        MenuManagement.invalidOptionText();
        Thread.sleep(SLEEP_SHORT);
    }
}