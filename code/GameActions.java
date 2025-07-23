package code;

import code.models.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class GameActions {
    private static final int SLEEP_SHORT = 1000; // 1 second

    public static void table(Player player, double bet) throws IOException, InterruptedException {
        TableActions table = new TableActions();
        table.initializeDeck(1); // Initialize and shuffle a single deck

        // Deal initial cards
        List<String> playerCards = new ArrayList<>();
        List<String> dealerCards = new ArrayList<>();

        // Deal two cards to each player alternately
        playerCards.add(table.drawCard());
        dealerCards.add(table.drawCard());
        playerCards.add(table.drawCard());
        dealerCards.add(table.drawCard());

        int dealerCardCount = 2;
        int playerCardCount = 2;
        boolean gameActive = true;
        boolean insuranceAvailable = true;
        double insurance = 0.0;

        // Check for player blackjack first
        if (table.isBlackjack(playerCards)) {
            MenuManagement.printFullBoard(dealerCards, playerCards, dealerCardCount, playerCardCount, false, table);
            // Check if dealer also has blackjack
            if (table.isBlackjack(dealerCards)) {
                System.out.println("Both have Blackjack! Push!");
                player.setBalance(player.getBalance() + bet);
            } else {
                System.out.println("Blackjack! You win 3:2 payout!");
                player.setBalance(player.getBalance() + bet * 2.5);
                player.addWin();
            }
            SaveManagement.savePlayerInfo(player);
            Thread.sleep(SLEEP_SHORT * 2);
            return;
        }

        // Check for dealer blackjack if upcard is Ace
        if (dealerCards.get(0).startsWith("A")) {
            // Offer insurance first (before checking for blackjack)
            boolean insuranceDecided = false;
            while (!insuranceDecided) {
                System.out.print("Dealer shows Ace. Want insurance? (Y/N): ");
                Scanner userInput = new Scanner(System.in);
                String choice = userInput.next().trim().toLowerCase();

                if (choice.equals("y")) {
                    boolean validInsurance = false;
                    while (!validInsurance) {
                        System.out.print("Enter insurance amount (up to " + (bet/2) + "): ");
                        try {
                            insurance = userInput.nextDouble();
                            if (insurance > 0 && insurance <= bet/2) {
                                player.setBalance(player.getBalance() - insurance);
                                System.out.println("Insurance placed: " + insurance);
                                validInsurance = true;
                                insuranceAvailable = false;
                            } else {
                                System.out.println("Invalid insurance amount. Must be between 0 and " + (bet/2));
                            }
                        } catch (InputMismatchException e) {
                            System.out.println("Please enter a valid number");
                            userInput.next(); // Clear invalid input
                        }
                    }
                    insuranceDecided = true;
                } else if (choice.equals("n")) {
                    insuranceDecided = true;
                } else {
                    System.out.println("Please enter Y or N");
                }
            }
            // Now check for dealer blackjack
            if (table.isBlackjack(dealerCards)) {
                MenuManagement.printFullBoard(dealerCards, playerCards, dealerCardCount, playerCardCount, false, table);
                System.out.println("Dealer has Blackjack!");

                if (insurance > 0) {
                    System.out.println("Insurance pays 2:1");
                    player.setBalance(player.getBalance() + insurance * 3);
                }
                if (table.isBlackjack(playerCards)) {
                    System.out.println("Both have Blackjack! Push!");
                    player.setBalance(player.getBalance() + bet);
                } else {
                    player.addLoss();
                }
                SaveManagement.savePlayerInfo(player);
                Thread.sleep(SLEEP_SHORT * 2);
                return;
            }
        // Check for dealer blackjack if upcard is 10-value (but no insurance offered)
        } else if (isTenValueCard(dealerCards.get(0))) {
            if (table.isBlackjack(dealerCards)) {
                MenuManagement.printFullBoard(dealerCards, playerCards, dealerCardCount, playerCardCount, false, table);
                System.out.println("Dealer has Blackjack!");

                if (table.isBlackjack(playerCards)) {
                    System.out.println("Both have Blackjack! Push!");
                    player.setBalance(player.getBalance() + bet);
                } else {
                    player.addLoss();
                }

                SaveManagement.savePlayerInfo(player);
                Thread.sleep(SLEEP_SHORT * 2);
                return;
            }
        }

        // Main game loop
        while (gameActive) {
            MenuManagement.printFullBoard(dealerCards, playerCards, dealerCardCount, playerCardCount, true, table);
            MenuManagement.showGameActionsMenu();

            Scanner userInput = new Scanner(System.in);
            String action = userInput.next().trim().toLowerCase();

            switch (action) {
                case "1": // Hit
                    playerCards.add(table.drawCard());
                    playerCardCount++;

                    int playerTotal = table.sumCards(playerCards);
                    MenuManagement.printFullBoard(dealerCards, playerCards, dealerCardCount, playerCardCount, false, table);

                    if (playerTotal > 21) {
                        handleBust(player, playerCards, dealerCards, playerCardCount, dealerCardCount, table, bet);
                        gameActive = false;
                    } else if (playerTotal == 21) {
                        // Player can't hit on 21, auto stand
                        gameActive = stand(player, dealerCards, playerCards, table, insurance, bet);
                    }
                    break;

                case "2": // Stand
                    gameActive = stand(player, dealerCards, playerCards, table, insurance, bet);
                    break;

                case "3": // Double Down
                    if (playerCardCount == 2 && player.canAfford(bet) &&
                            table.sumCards(playerCards) <= 11) {

                        player.setBalance(player.getBalance() - bet);
                        bet *= 2;
                        playerCards.add(table.drawCard());
                        playerCardCount++;
                        MenuManagement.printFullBoard(dealerCards, playerCards, dealerCardCount, playerCardCount, false, table);

                        if (table.sumCards(playerCards) > 21) {
                            handleBust(player, playerCards, dealerCards, playerCardCount, dealerCardCount, table, bet);
                        } else {
                            gameActive = stand(player, dealerCards, playerCards, table, insurance, bet);
                        }
                    } else {
                        System.out.println("Cannot double down - check your hand value or balance");
                        Thread.sleep(SLEEP_SHORT);
                    }
                    break;

                case "4": // Surrender
                    System.out.print("Are you sure you want to surrender? (Y/N): ");
                    if (userInput.next().trim().equalsIgnoreCase("Y")) {
                        player.setBalance(player.getBalance() + bet / 2);
                        System.out.println("Surrendered. You get half your bet back.");
                        SaveManagement.savePlayerInfo(player);
                        gameActive = false;
                        Thread.sleep(SLEEP_SHORT);
                    }
                    break;

                case "h": // Help
                    showHelpMenu();
                    break;

                default:
                    System.out.println("Invalid option");
                    Thread.sleep(SLEEP_SHORT);
            }
        }
    }

    private static boolean isTenValueCard(String card) {
        String rank = card.substring(0, card.length() - 1);
        return rank.equals("10") || rank.equals("J") || rank.equals("Q") || rank.equals("K");
    }

    private static void handleBust(Player player, List<String> playerCards, List<String> dealerCards,
                                   int playerCardCount, int dealerCardCount, TableActions table, double bet)
            throws InterruptedException, IOException {
        MenuManagement.printFullBoard(dealerCards, playerCards, dealerCardCount, playerCardCount, false, table);
        System.out.println("Bust! You lose your bet.");
        player.addLoss();
        SaveManagement.savePlayerInfo(player);
        Thread.sleep(SLEEP_SHORT * 2);
    }

    private static boolean stand(Player player, List<String> dealerCards, List<String> playerCards,
                                 TableActions table, double insurance, double bet)
            throws InterruptedException, IOException {

        // Reveal dealer's hole card
        MenuManagement.printFullBoard(dealerCards, playerCards, dealerCards.size(), playerCards.size(), false, table);

        // Dealer draws until 17 or higher
        while (table.shouldDealerHit(table.sumCards(dealerCards))) {
            Thread.sleep(SLEEP_SHORT);
            System.out.println("Dealer hits...");
            dealerCards.add(table.drawCard());
            MenuManagement.printFullBoard(dealerCards, playerCards, dealerCards.size(), playerCards.size(), false, table);
            Thread.sleep(SLEEP_SHORT);
        }

        // Determine winner
        int playerTotal = table.sumCards(playerCards);
        int dealerTotal = table.sumCards(dealerCards);

        if (dealerTotal > 21) {
            System.out.println("Dealer busts! You win!");
            player.setBalance(player.getBalance() + bet * 2);
            player.addWin();
        } else if (playerTotal > dealerTotal) {
            System.out.println("You win!");
            player.setBalance(player.getBalance() + bet * 2);
            player.addWin();
        } else if (playerTotal < dealerTotal) {
            System.out.println("Dealer wins!");
            player.addLoss();
        } else {
            System.out.println("Push! It's a tie.");
            player.setBalance(player.getBalance() + bet);
        }

        // Handle insurance if applicable
        if (insurance > 0 && table.isBlackjack(dealerCards)) {
            System.out.println("Insurance pays 2:1");
            player.setBalance(player.getBalance() + insurance * 2);
        }

        SaveManagement.savePlayerInfo(player);
        Thread.sleep(SLEEP_SHORT * 2);
        return false; // Game is over
    }

    private static double handleInsurance(Player player, double bet, Scanner input)
            throws InterruptedException {
        System.out.print("Enter insurance amount (up to " + (bet/2) + "): ");
        double insurance = input.nextDouble();
        if (insurance > 0 && insurance <= bet/2) {
            player.setBalance(player.getBalance() - insurance);
            System.out.println("Insurance placed: " + insurance);
            return insurance;
        } else {
            System.out.println("Invalid insurance amount");
            return 0;
        }
    }

    private static void showHelpMenu() throws InterruptedException {
        MenuManagement.showHelpMenu();
        Scanner input = new Scanner(System.in);
        while (!input.next().trim().equalsIgnoreCase("x")) {
            Thread.sleep(SLEEP_SHORT);
        }
    }
}