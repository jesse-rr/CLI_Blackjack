import java.io.IOException;
import java.util.List;

public class PrintASCII {

    public static List<String> printCards(List<String> deck, int numCards, boolean isDealerCards, int showCardNumber) {
        int cardWidth = 19;
        int cardHeight = 13;
        printTopBorder(cardWidth, numCards, isDealerCards);
        System.out.println();
        for (int i = 0; i < cardHeight - 2; i++) {
            printCardLine(deck.subList(0, numCards), i, numCards, isDealerCards, showCardNumber);
        }
        printBottomBorder(cardWidth, numCards, isDealerCards);
        System.out.println();

        return deck;
    }

    private static void printCardLine(List<String> cards, int row, int numCards, boolean isDealerCards, int showCardNumber) {
        for (int i = 0; i < numCards; i++) {
            String card = cards.get(i);
            String rank = card.substring(0, card.length() - 1);
            String suit = card.substring(card.length() - 1);

            if (isDealerCards && i == 1) {
                if (row == 5 && i == numCards - 1) {
                    System.out.printf("│█████████████████│ Total value: [ %s ]", showCardNumber);
                } else {
                    System.out.print("│█████████████████│ ");
                }
            } else {
                System.out.print("│");

                if (row == 0) {
                    if (rank.equals("10")) {
                        System.out.printf(" %-3s             │", rank);
                    } else {
                        System.out.printf(" %-2s              │", rank);
                    }
                } else if (row == 5) {
                    if (i == numCards - 1) {
                        System.out.printf("        %-2s       │ Total value: [ %s ]", suit, showCardNumber);
                    } else {
                        System.out.printf("        %-2s       │", suit);
                    }
                } else if (row == 10) {
                    if (rank.equals("10")) {
                        System.out.printf("              %-3s│", rank);
                    } else {
                        System.out.printf("               %-2s│", rank);
                    }
                } else {
                    System.out.print("                 │");
                }
                System.out.print(" ");
            }
        }
        System.out.println();
    }

    private static void printTopBorder(int cardWidth, int numCards, boolean isDealerCards) {
        for (int j = 0; j < numCards; j++) {
            System.out.print("┌");
            if (j == 1 && isDealerCards) {
                for (int i = 1; i < cardWidth - 1; i++) {
                    System.out.print("▄");
                }
                System.out.print("┐");
            } else {
                for (int i = 1; i < cardWidth - 1; i++) {
                    System.out.print("─");
                }
                System.out.print("┐");
            }
            System.out.print(" ");
        }
    }

    private static void printBottomBorder(int cardWidth, int numCards, boolean isDealerCards) {
        for (int j = 0; j < numCards; j++) {
            System.out.print("└");
            if (j == 1 && isDealerCards) {
                for (int i = 1; i < cardWidth - 1; i++) {
                    System.out.print("▀");
                }
                System.out.print("┘");
            } else {
                for (int i = 1; i < cardWidth - 1; i++) {
                    System.out.print("─");
                }
                System.out.print("┘");
            }
            System.out.print(" ");
        }
    }

    public static void printlnMenu(int menuNumber) throws InterruptedException {
        switch (menuNumber) {
            case 1:
                flushConsole();
                System.out.println("""
                    ╟►────────────────────────────────◄═══════════[[████]]══════════►─────────────────────────────────────◄╣

                                    ███╗   ███╗ █████╗ ██╗███╗   ██╗    ███╗   ███╗███████╗███╗   ██╗██╗   ██╗
                                    ████╗ ████║██╔══██╗██║████╗  ██║    ████╗ ████║██╔════╝████╗  ██║██║   ██║
                                    ██╔████╔██║███████║██║██╔██╗ ██║    ██╔████╔██║█████╗  ██╔██╗ ██║██║   ██║
                                    ██║╚██╔╝██║██╔══██║██║██║╚██╗██║    ██║╚██╔╝██║██╔══╝  ██║╚██╗██║██║   ██║
                                    ██║ ╚═╝ ██║██║  ██║██║██║ ╚████║    ██║ ╚═╝ ██║███████╗██║ ╚████║╚██████╔╝
                                    ╚═╝     ╚═╝╚═╝  ╚═╝╚═╝╚═╝  ╚═══╝    ╚═╝     ╚═╝╚══════╝╚═╝  ╚═══╝ ╚═════╝

                      Start Game (S)
                      Account (A)
                      Exit (X)
                      [Type 'h' for help/how to play]

                    ╟►────────────────────────────────◄═══════════[[████]]══════════►─────────────────────────────────────◄╣
                    """);
                break;
            case 2:
                flushConsole();
                System.out.print("""
                    ╟►────────────────────────────────◄═══════════[[████]]══════════►─────────────────────────────────────◄╣

                     ▀█████████▄   ▄█          ▄████████  ▄████████    ▄█   ▄█▄      ▄█    ▄████████ ▄████████    ▄█   ▄█▄
                       ███    ███ ███         ███    ███ ███    ███   ███ ▄███▀     ███   ███    ███ ███    ███   ███ ▄███▀
                       ███    ███ ███         ███    ███ ███    █▀    ███▐██▀       ███   ███    ███ ███    █▀    ███▐██▀
                      ▄███▄▄▄██▀  ███         ███    ███ ███         ▄█████▀        ███   ███    ███ ███         ▄█████▀
                     ▀▀███▀▀▀██▄  ███       ▀███████████ ███        ▀▀█████▄        ███ ▀███████████ ███        ▀▀█████▄
                       ███    ██▄ ███         ███    ███ ███    █▄    ███▐██▄       ███   ███    ███ ███    █▄    ███▐██▄
                       ███    ███ ███▌    ▄   ███    ███ ███    ███   ███ ▀███▄     ███   ███    ███ ███    ███   ███ ▀███▄
                     ▄█████████▀  █████▄▄██   ███    █▀  ████████▀    ███   ▀█▀ █▄ ▄███   ███    █▀  ████████▀    ███   ▀█▀

                                              Blackjack (21) is a card game, where the goal is
                                            getting as close to 21 as possible without going over.
                                                  Cards 2-10 are worth their face value,
                             face cards (Jack, Queen, King) are 10 points, and Aces are either 1 (soft-ace) or 11.
                                               Players are dealt two cards and can choose to:
                                                   [ Hit， Stand， Double Down， or Slip ]
                                     To win, beat the dealer’s hand or hit a Blackjack (Ace + 10) = (21)
            
                                         «［ Here are the key commands you can use during the game! ］»
                      ■ 𝗛𝗶𝘁 = Take another card to improve your hand, after, you can only Hit or Stand.
                      ■ 𝗦𝘁𝗮𝗻𝗱 = Keep your current hand, no more cards.
                      ■ 𝗗𝗼𝘂𝗯𝗹𝗲 𝗱𝗼𝘄𝗻 = Double your bet and take ONE more card. Only possible if hand value < 12
                      ■ 𝗦𝗽𝗹𝗶𝘁 = Divide a pair of cards into two separate hands, each with its own bet. [NOT IMPLEMENTED]
                      ■ 𝗦𝘂𝗿𝗿𝗲𝗻𝗱𝗲𝗿 = Forfeit your hand and get half your bet back.
                      ■ 𝗜𝗻𝘀𝘂𝗿𝗮𝗻𝗰𝗲 = Bet on the dealer having a blackjack if their upcard is an Ace.
            
                                                            «[ Additional Tips ]»
                                                  The dealer hits if total card value < 17.
                                           If a individual has 2 Aces, it will count as 12, not 22.
                                If the dealer has an Ace, he will check for a blackjack, if he has, game ends.
                               Don’t split Aces and 8s too often: Only split if the dealer’s card is weak (2-6).
                                 Stand on 12-16 if the dealer shows 2-6: The dealer is more likely to lose.

                                                       Enjoy the game, and good luck!
                                                             Press (X) to Return

                    ╟►────────────────────────────────◄═══════════[[████]]══════════►─────────────────────────────────────◄╣
                    """);
                break;
            case 3:
                flushConsole();
                System.out.println("""
                    ╟►────────────────────────────────◄═══════════[[████]]══════════►─────────────────────────────────────◄╣
                                                            ♣  ♠  Blackjack  ♥  ♦

                                                               [S] - Sign in
                                                                [L] - Login
                                                             [G] - Play as Guest
                                                                 (X) - EXIT

                    ╟►────────────────────────────────◄═══════════[[████]]══════════►─────────────────────────────────────◄╣
                    """);
                break;
            case 4:
                flushConsole();
                System.out.print("""
                    ╟►────────────────────────────────◄═══════════[[████]]══════════►─────────────────────────────────────◄╣
                                                                🃏  Dealer  🃏
                    """);
                break;
            case 5:
                System.out.println("""
                                                                ♛  Player  ♛
                    ╟►────────────────────────────────◄═══════════[[████]]══════════►─────────────────────────────────────◄╣
                    """);
                break;
        }
    }
    
    public static void printFMenu(Player player, int menuNumber) throws InterruptedException {
        flushConsole();
        switch (menuNumber) {
            case 1:
                System.out.printf("""
                    ╟►────────────────────────────────◄═══════════[[████]]══════════►─────────────────────────────────────◄╣
                                                                   Account
                         ■ Name: %s
                         ■ Balance: %s
                         ■ Win Rate: %s
                         ■ Wins: %s
                         ■ Loses: %s
                                                            Press (X) to Return
                    ╟►────────────────────────────────◄═══════════[[████]]══════════►─────────────────────────────────────◄╣
                    """, player.getName(), "R$ " + player.getBalance(), player.getWinRate() + "%", player.getWins(), player.getLoses());
                break;
            case 2:
                System.out.printf("""
                    ╟►────────────────────────────────◄═══════════[[████]]══════════►─────────────────────────────────────◄╣

                                 ██╗███╗   ██╗██╗████████╗██╗ █████╗ ██╗         ██████╗ ███████╗████████╗
                                 ██║████╗  ██║██║╚══██╔══╝██║██╔══██╗██║         ██╔══██╗██╔════╝╚══██╔══╝
                                 ██║██╔██╗ ██║██║   ██║   ██║███████║██║         ██████╔╝█████╗     ██║
                                 ██║██║╚██╗██║██║   ██║   ██║██╔══██║██║         ██╔══██╗██╔══╝     ██║
                                 ██║██║ ╚████║██║   ██║   ██║██║  ██║███████╗    ██████╔╝███████╗   ██║
                                 ╚═╝╚═╝  ╚═══╝╚═╝   ╚═╝   ╚═╝╚═╝  ╚═╝╚══════╝    ╚═════╝ ╚══════╝   ╚═╝

                     Your balance: R$ %s
                    ╟►────────────────────────────────◄═══════════[[████]]══════════►─────────────────────────────────────◄╣
                    """, player.getBalance());
                break;
        }
    }

    private static void flushConsole() throws InterruptedException {
        String os_name = System.getProperty("os.name").toLowerCase();
        if (os_name.contains("win")) {
            try {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.print("\033[H\033[2J");
            System.out.flush();
        }
    }

    public static void printFullBoard(List<String> dealer_cards, List<String> player_cards, int dealerCardNumber, int playerCardNumber, boolean isDealerCards, TableActions table) throws InterruptedException {
        int sum;
        if (isDealerCards) {
            sum = table.sumCards(dealer_cards.subList(0, dealerCardNumber - 1));
        } else {
            sum = table.sumCards(dealer_cards.subList(0, dealerCardNumber));
        }
        PrintASCII.printlnMenu(4);
        PrintASCII.printCards(dealer_cards, dealerCardNumber, isDealerCards, sum);
        PrintASCII.printCards(player_cards, playerCardNumber, false, table.sumCards(player_cards.subList(0, playerCardNumber)));
        PrintASCII.printlnMenu(5);
    }
}
