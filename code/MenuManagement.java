package code;

import code.models.MenuType;

import java.io.IOException;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class MenuManagement {
    private static final Map<MenuType, String> MENU_TEMPLATES = initializeMenuTemplates();

    private static Map<MenuType, String> initializeMenuTemplates() {
        Map<MenuType, String> map = new EnumMap<>(MenuType.class);
        map.put(MenuType.MAIN_MENU, MAIN_MENU);
        map.put(MenuType.HELP_MENU, HELP_MENU);
        map.put(MenuType.USER_ACCESS_MENU, USER_ACCESS_MENU);
        map.put(MenuType.DEALER_TOP_MENU, DEALER_TOP_MENU);
        map.put(MenuType.PLAYER_BOTTOM_MENU, PLAYER_BOTTOM_MENU);
        map.put(MenuType.INITIAL_BET_MENU, INITIAL_BET_MENU);
        map.put(MenuType.ACCOUNT_INFO_MENU, ACCOUNT_INFO_MENU);
        map.put(MenuType.GAME_ACTIONS_MENU, GAME_ACTIONS_MENU);
        map.put(MenuType.INVALID_OPTION_TEXT, INVALID_OPTION_TEXT);
        return map;
    }

    public static void displayMenu(MenuType menuType, Object... args) {
        String template = MENU_TEMPLATES.get(menuType);
        if (template != null) {
            System.out.printf(template + "\n", args);
        } else {
            System.out.println("Menu not found: " + menuType);
        }
    }

    public static void printFullBoard(List<String> dealer_cards, List<String> player_cards, int dealerCardNumber, int playerCardNumber, boolean isDealerCards, TableActions table) {
        int sum;
        if (isDealerCards) {
            sum = table.sumCards(dealer_cards.subList(0, dealerCardNumber - 1));
        } else {
            sum = table.sumCards(dealer_cards.subList(0, dealerCardNumber));
        }
        showDealerTopMenu();
        CardManagement.printCards(dealer_cards, dealerCardNumber, isDealerCards, sum);
        CardManagement.printCards(player_cards, playerCardNumber, false, table.sumCards(player_cards.subList(0, playerCardNumber)));
        showPlayerBottomMenu();
    }

    public static void clearScreen() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Screen clearing interrupted");
        } catch (IOException e) {
            System.out.println("Failed to clear screen: " + e.getMessage());
        }
    }

    public static void showMainMenu() {
        clearScreen();
        displayMenu(MenuType.MAIN_MENU);
    }

    public static void showHelpMenu() {
        clearScreen();
        displayMenu(MenuType.HELP_MENU);
    }

    public static void showUserAccessMenu() {
        clearScreen();
        displayMenu(MenuType.USER_ACCESS_MENU);
    }

    public static void showDealerTopMenu() {
        displayMenu(MenuType.DEALER_TOP_MENU);
    }

    public static void showPlayerBottomMenu() {
        displayMenu(MenuType.PLAYER_BOTTOM_MENU);
    }

    public static void showInitialBetMenu(double balance) {
        clearScreen();
        displayMenu(MenuType.INITIAL_BET_MENU, balance);
    }

    public static void showAccountInfoMenu(String name, double balance, double winRate, int wins, int loses) {
        clearScreen();
        displayMenu(MenuType.ACCOUNT_INFO_MENU, name, balance, winRate, wins, loses);
    }

    public static void showGameActionsMenu() {
        displayMenu(MenuType.GAME_ACTIONS_MENU);
    }

    public static void invalidOptionText() {
        displayMenu(MenuType.INVALID_OPTION_TEXT);
    }

    // TEMPLATES
    private static final String MAIN_MENU = """
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
            """;

    private static final String HELP_MENU = """
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
                         To win, beat the dealer's hand or hit a Blackjack (Ace + 10) = (21)
    
                                     «［ Here are the key commands you can use during the game! ］»
              ■ 𝗛𝗶𝘁 = Take another card to improve your hand, after, you can only Hit or Stand.
              ■ 𝗦𝘁𝗮𝗻𝗱 = Keep your current hand, no more cards.
              ■ 𝗗𝗼𝘂𝗯𝗹𝗲 𝗱𝗼𝘄𝗻 = Double your bet and take ONE more card. Only possible if hand value < 12
              ■ 𝗦𝗽𝗹𝗶𝘁 =̶ D̶i̶v̶i̶d̶e̶ a̶ p̶a̶i̶r̶ o̶f̶ c̶a̶r̶d̶s̶ i̶n̶t̶o̶ t̶w̶o̶ s̶e̶p̶a̶r̶a̶t̶e̶ h̶a̶n̶d̶s̶, e̶a̶c̶h̶ w̶i̶t̶h̶ i̶t̶s̶ o̶w̶n̶ b̶e̶t̶. [̶N̶O̶T̶ I̶M̶P̶L̶E̶M̶E̶N̶T̶E̶D̶]̶
              ■ 𝗦𝘂𝗿𝗿𝗲𝗻𝗱𝗲𝗿 = Forfeit your hand and get half your bet back.
              ■ 𝗜𝗻𝘀𝘂𝗿𝗮𝗻𝗰𝗲 = Bet on the dealer having a blackjack if their upcard is an Ace.
    
                                                     «[ Additional Tips ]»
                                            The dealer hits if total card value < 17.
                                     If a individual has 2 Aces, it will count as 12, not 22.
                          If the dealer has an Ace, he will check for a blackjack, if he has, game ends.
                         Don't split Aces and 8s too often: Only split if the dealer's card is weak (2-6).
                           Stand on 12-16 if the dealer shows 2-6: The dealer is more likely to lose.

                                                 Enjoy the game, and good luck!
                                                      Press (X) to Return

            ╟►────────────────────────────────◄═══════════[[████]]══════════►─────────────────────────────────────◄╣
            """;

    private static final String USER_ACCESS_MENU = """
            ╟►────────────────────────────────◄═══════════[[████]]══════════►─────────────────────────────────────◄╣
                                                    ♣  ♠  Blackjack  ♥  ♦

                                                       [S] - Sign in
                                                        [L] - Login
                                                     [G] - Play as Guest
                                                         (X) - EXIT

            ╟►────────────────────────────────◄═══════════[[████]]══════════►─────────────────────────────────────◄╣
            """;

    private static final String DEALER_TOP_MENU = """
            ╟►────────────────────────────────◄═══════════[[████]]══════════►─────────────────────────────────────◄╣
                                                       🃏  Dealer  🃏
            """;

    private static final String PLAYER_BOTTOM_MENU = """
                                                        ♛  Player  ♛
            ╟►────────────────────────────────◄═══════════[[████]]══════════►─────────────────────────────────────◄╣
            """;

    private static final String INITIAL_BET_MENU = """
            ╟►────────────────────────────────◄═══════════[[████]]══════════►─────────────────────────────────────◄╣

                             ██╗███╗   ██╗██╗████████╗██╗ █████╗ ██╗         ██████╗ ███████╗████████╗
                             ██║████╗  ██║██║╚══██╔══╝██║██╔══██╗██║         ██╔══██╗██╔════╝╚══██╔══╝
                             ██║██╔██╗ ██║██║   ██║   ██║███████║██║         ██████╔╝█████╗     ██║
                             ██║██║╚██╗██║██║   ██║   ██║██╔══██║██║         ██╔══██╗██╔══╝     ██║
                             ██║██║ ╚████║██║   ██║   ██║██║  ██║███████╗    ██████╔╝███████╗   ██║
                             ╚═╝╚═╝  ╚═══╝╚═╝   ╚═╝   ╚═╝╚═╝  ╚═╝╚══════╝    ╚═════╝ ╚══════╝   ╚═╝

                 Your balance - R$: %s
            ╟►────────────────────────────────◄═══════════[[████]]══════════►─────────────────────────────────────◄╣
            """;

    private static final String ACCOUNT_INFO_MENU = """
            ╟►────────────────────────────────◄═══════════[[████]]══════════►─────────────────────────────────────◄╣
                                                               Account
                     ■ Name: %s
                     ■ Balance: R$ %s
                     ■ Win Rate: %s%%
                     ■ Wins: %s
                     ■ Loses: %s
                                                        Press (X) to Return
            ╟►────────────────────────────────◄═══════════[[████]]══════════►─────────────────────────────────────◄╣
            """;

    private static final String GAME_ACTIONS_MENU = """
            (1) - Hit
            (2) - Stand
            (3) - Double Down
            (4) - Surrender
            (5) - Insurance
            [press 'h' for help]
            """;

    private static final String INVALID_OPTION_TEXT = "[══► Not a valid option! Try again ◄══]";
}