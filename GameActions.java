import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class GameActions {

    public static void table(Player player, double bet) throws IOException, InterruptedException {
        TableActions table = new TableActions();
        List<String> deck = table.createDeck();
        List<String> dealer_cards = table.setDealerCards(deck);
        List<String> player_cards = table.setPlayerCards(deck);
        int dealerCardNumber = 2;
        int playerCardNumber = 2;
        boolean noWinner = true;
        boolean noInsurance = true;
        double insurance = 0.0;
        String confirm = "";
//        int maxSplitTimes = 3; // TODO maybe?

        do {
            PrintASCII.printFullBoard(dealer_cards,player_cards,dealerCardNumber,playerCardNumber,true,table);

            Scanner userInput = new Scanner(System.in);
            System.out.println("""
                    (1) - Hit
                    (2) - Stand
                    (3) - Double Down
                    (4) - Surrender
                    (5) - Insurance
                    [press 'h' for help]
                    """);
            String decision = userInput.next().trim().toLowerCase();
            switch (decision) {
                case "1":
                    noWinner = hitLogic(player_cards, dealer_cards, table, insurance, player, bet, dealerCardNumber, playerCardNumber);
                    break;
                case "2":
                    noWinner = standLogic(dealer_cards, player_cards, table, insurance, player, bet, dealerCardNumber, playerCardNumber);
                    break;
                case "3":
                    noWinner = doubleDownLogic(dealer_cards, player_cards, table, insurance, player, bet, dealerCardNumber, playerCardNumber);
                    break;
//                case "4":
//                        if (table.isTwoAces(player_cards.subList(0, playerCardNumber))) {
//                            return;
//                        }
//                        table.canSplit(player_cards, playerCardNumber);
//                        splitLogic(dealer_cards, player_cards, table, insurance, player, bet, dealerCardNumber, playerCardNumber, maxSplitTimes);
//                    break;
                case "4":
                    System.out.print("Are you sure of Surrender? I'll not count as a loss, but half your bet will be lost. Y/N: ");
                    confirm = userInput.next().trim().toLowerCase().substring(0, 1);
                    switch (confirm) {
                        case "y":
                            player.setBalance(player.getBalance() + bet / 2);
                            Startup.savePlayerInfo(player);
                            noWinner = false;
                            break;
                    }
                    break;
                case "5":
                    if (noInsurance) {
                        insurance = insuranceLogic(dealer_cards, bet);
                        if (insurance > 0) {
                            noInsurance = false;
                            Thread.sleep(1000);
                            System.out.println("Checking for blackjack (A + 10)");
                            Thread.sleep(3000);
                            PrintASCII.printFullBoard(dealer_cards,player_cards,dealerCardNumber,playerCardNumber,false,table);
                            if (table.isBlackjack(dealer_cards.subList(0, dealerCardNumber)) && !table.isBlackjack(player_cards.subList(0, playerCardNumber))) {
                                Thread.sleep(1000);
                                System.out.println("Dealer has a blackjack! Insurance pays out 2:1.");
                                player.setBalance(player.getBalance() + insurance * 2);
                                Startup.savePlayerInfo(player);
                            } else if (!table.isBlackjack(dealer_cards.subList(0, dealerCardNumber))) {
                                Thread.sleep(1000);
                                System.out.println("Dealer does not have a blackjack. Insurance bet lost.");
                                player.setBalance(player.getBalance() - insurance);
                                Startup.savePlayerInfo(player);
                            } else if (table.isBlackjack(dealer_cards.subList(0, dealerCardNumber)) && table.isBlackjack(player_cards.subList(0, playerCardNumber))) {
                                Thread.sleep(1000);
                                System.out.println("Dealer has a blackjack, but you do too, its a DRAW, but Insurance pays out 2:1.");
                                player.setBalance(player.getBalance() + bet + insurance * 2);
                                Startup.savePlayerInfo(player);
                            }
                            noWinner = false;
                        }
                    } else {
                        System.out.println("You're already insured!");
                    }
                    Thread.sleep(2000);
                    break;
                case "h":
                    boolean exit = true;
                    do {
                        PrintASCII.printlnMenu(2);
                        confirm = userInput.next().trim().toLowerCase();
                        switch (confirm) {
                            case "x":
                                exit = false;
                                break;
                            default:
                                System.out.println("Not a valid option! Try again");
                                Thread.sleep(1000);
                        }
                    } while (exit);
                    break;
                default:
                    System.out.println("Not an valid option! Try again");
                    Thread.sleep(1000);
            }
        } while (noWinner);
    }

//    private static void splitLogic(List<String> dealer_cards, List<String> player_cards, TableActions table, double insurance, Player player, double bet, int dealerCardNumber, int playerCardNumber, int maxSplitTimes) {
//        for (int i = 0; i<maxSplitTimes; i++) {
//            Scanner userInput = new Scanner(System.in);
//            System.out.printf("Place bet for hand [ %s ]: ",i);
//            PrintASCII.printlnMenu(4);
//            PrintASCII.printCards(dealer_cards, dealerCardNumber, true, table.sumCards(dealer_cards.subList(0, dealerCardNumber - 1)));
//            PrintASCII.printCards(player_cards, playerCardNumber, false, table.sumCards(player_cards.subList(0, playerCardNumber)));
//            PrintASCII.printlnMenu(5);
//
//            double new_bet = Double.parseDouble(userInput.nextLine().trim().replace(',','.'));
//
//        }
//    } TODO maybe?

    private static boolean hitLogic(List<String> player_cards, List<String> dealer_cards, TableActions table, double insurance, Player player, double bet, int dealerCardNumber, int playerCardNumber) throws InterruptedException, IOException {
        PrintASCII.printFullBoard(dealer_cards,player_cards,dealerCardNumber,playerCardNumber,true,table);

        int playerTotal = table.sumCards(player_cards.subList(0, playerCardNumber));
        boolean isPlaying = true;

        while (isPlaying && playerTotal <= 21) {
            System.out.println("Player hits!");
            Thread.sleep(1000);

            playerTotal = table.sumCards(player_cards.subList(0, playerCardNumber += 1));
            PrintASCII.printFullBoard(dealer_cards,player_cards,dealerCardNumber,playerCardNumber,true,table);
            Thread.sleep(1000);

            if (playerTotal > 21) {
                System.out.println("Player has gone over 21! Dealer wins.");
                System.out.println("+ 1 Loss");
                Thread.sleep(1000);
                return verifyWinnerPayments(2, player, insurance, bet, table, player_cards, dealer_cards);
            }

            System.out.println("""
                 ┏┳┓       ┏┓   •
                  ┃ ┓┏┏┏┓  ┃┃┏┓╋┓┏┓┏┓┏
                  ┻ ┗┻┛┗┛  ┗┛┣┛┗┗┗┛┛┗┛
                             ┛
                 (1) - Hit | Stand - (2)
                """);
            Scanner userInput = new Scanner(System.in);
            String decision = userInput.next().trim().substring(0, 1);

            switch (decision) {
                case "1":
                    break;
                case "2":
                    isPlaying = false;
                    break;
                default:
                    System.out.println("Invalid input. Please choose (1) for Hit or (2) for Stand.");
                    Thread.sleep(1000);
            }
        }

        return standLogic(dealer_cards, player_cards, table, insurance, player, bet, dealerCardNumber, playerCardNumber);
    }

    public static boolean doubleDownLogic(List<String> dealer_cards, List<String> player_cards, TableActions table, double insurance, Player player, double bet, int dealerCardNumber, int playerCardNumber) throws IOException, InterruptedException {
        if (player.getBalance() < bet) {
            System.out.println("You don't have enough balance to double down!");
            Thread.sleep(1000);
            return true;
        }

        if (!table.canDoubleDown(player_cards, playerCardNumber)) {
            System.out.println("You can only double down if your total is 11 or less.");
            Thread.sleep(1000);
            return true;
        }

        System.out.println("Player chooses to double down. One more card will be dealt!");
        Thread.sleep(1000);

        player.setBalance(player.getBalance() - bet);
        bet = bet * 2;
        int playerCardTotal = table.sumCards(player_cards.subList(0, playerCardNumber+=1));

        PrintASCII.printFullBoard(dealer_cards,player_cards,dealerCardNumber,playerCardNumber,true,table);

        if (playerCardTotal > 21) {
            System.out.println("Player has gone over 21! Dealer wins.");
            System.out.println("+ 1 Loss");
            Thread.sleep(1000);
            return verifyWinnerPayments(2, player, insurance, bet, table, player_cards, dealer_cards);
        }

        return standLogic(dealer_cards, player_cards, table, insurance, player, bet, dealerCardNumber, playerCardNumber);
    }



    private static boolean standLogic(List<String> dealer_cards, List<String> player_cards, TableActions table, double insurance, Player player, double bet, int dealerCardNumber, int playerCardNumber) throws InterruptedException, IOException {
        PrintASCII.printFullBoard(dealer_cards,player_cards,dealerCardNumber,playerCardNumber,false,table);

        int winner = 0;

        int dealerTotal = table.sumCards(dealer_cards.subList(0, dealerCardNumber));

        while (dealerTotal < 17) {
            Thread.sleep(1000);
            System.out.println("Dealer hits!");
            Thread.sleep(1000);

            dealerTotal = table.sumCards(dealer_cards.subList(0, ++dealerCardNumber));
            PrintASCII.printFullBoard(dealer_cards,player_cards,dealerCardNumber,playerCardNumber,false,table);
            Thread.sleep(1000);
        }

        if (dealerTotal > 21) {
            Thread.sleep(1000);
            System.out.println("Dealer has gone over 21! Player wins.");
            System.out.println("+ 1 Win");
            Thread.sleep(1000);
            winner = 1;
        } else {
            Thread.sleep(1000);
            System.out.println("Dealer stands!");
            int playerTotal = table.sumCards(player_cards.subList(0, playerCardNumber));

            if (dealerTotal > playerTotal) {
                System.out.println("Dealer wins!");
                System.out.println("+ 1 Loss");
                Thread.sleep(1000);
                winner = 2;
            } else if (dealerTotal < playerTotal) {
                System.out.println("Player wins!");
                System.out.println("+ 1 Win");
                Thread.sleep(1000);
                winner = 1;
            } else {
                System.out.println("It's a tie!");
                winner = -1;
                Thread.sleep(1000);
            }
        }
        return verifyWinnerPayments(winner, player, insurance, bet, table, player_cards, dealer_cards);
    }

    public static double insuranceLogic(List<String> dealerCards, double bet) throws InterruptedException {
        double insuranceBet = 0.0;
        Scanner scanner = new Scanner(System.in);
        if (dealerCards.get(0).charAt(0) == 'A') {
            System.out.print("Dealer has an Ace. Do you want to take insurance? (Y/N): ");
            String decision = scanner.next().trim().toLowerCase();
            if (decision.equals("y")) {
                System.out.print("Enter your insurance bet (up to half of your original bet): ");
                insuranceBet = scanner.nextDouble();

                if (insuranceBet > 0 && insuranceBet <= bet / 2) {
                    System.out.println("Insurance bet placed: " + insuranceBet);
                } else {
                    System.out.println("Invalid insurance bet. It must be up to half of your original bet.");
                    insuranceBet = 0.0;
                }
            }
        } else {
            System.out.println("Insurance is only available when the dealer's upcard is an Ace.");
        }

        Thread.sleep(1000);
        return insuranceBet;
    }

    private static boolean verifyWinnerPayments(int winner, Player player, double insurance, double bet, TableActions table, List<String> player_cards, List<String> dealer_cards) throws IOException {
        if (winner == 1) {
            player.setWins(player.getWins() + 1);
            if (table.isBlackjack(player_cards)) {
                player.setBalance(player.getBalance() + bet * 2.5);
            } else {
                player.setBalance(player.getBalance() + bet * 2);
            }
        } else if (winner == 2) {
            player.setLoses(player.getLoses() + 1);
            if (table.isBlackjack(dealer_cards)) {
                player.setBalance(player.getBalance() + insurance * 2);
            }
        } else if (winner == -1) {
            player.setBalance(player.getBalance() + bet);
            if (insurance > 0 && table.isBlackjack(dealer_cards)) {
                player.setBalance(player.getBalance() + insurance * 2);
            }
        }

        player.setWinRate(Math.round(100.0 * player.getWins() / (player.getWins() + player.getLoses())));
        Startup.savePlayerInfo(player);
        return false;
    }

}
