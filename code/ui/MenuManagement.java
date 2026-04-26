package code.ui;

import code.models.enums.MenuType;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;

public class MenuManagement {
    public static final String RESET = "\u001B[0m";
    public static final String BOLD = "\u001B[1m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String MAGENTA = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";

    private static final Map<MenuType, String> MENU_TEMPLATES = initializeMenuTemplates();

    private static Map<MenuType, String> initializeMenuTemplates() {
        Map<MenuType, String> map = new EnumMap<>(MenuType.class);
        map.put(MenuType.MAIN_MENU, MAIN_MENU);
        map.put(MenuType.HELP_MENU, HELP_MENU);
        map.put(MenuType.USER_ACCESS_MENU, USER_ACCESS_MENU);
        map.put(MenuType.DEALER_TOP_MENU, DEALER_TOP_MENU);
        map.put(MenuType.PLAYER_BOTTOM_MENU, PLAYER_BOTTOM_MENU);
        map.put(MenuType.ACCOUNT_INFO_MENU, ACCOUNT_INFO_MENU);
        map.put(MenuType.GAME_ACTIONS_MENU, GAME_ACTIONS_MENU);
        map.put(MenuType.INVALID_OPTION_TEXT, INVALID_OPTION_TEXT);
        map.put(MenuType.MAP_MENU, MAP_MENU);
        map.put(MenuType.SKILL_SELECTION_MENU, SKILL_SELECTION_MENU);
        map.put(MenuType.DECK_SELECTION_MENU, DECK_SELECTION_MENU);
        return map;
    }

    public static void displayMenu(MenuType menuType, Object... args) {
        String template = MENU_TEMPLATES.get(menuType);
        if (template != null) {
            System.out.printf(template + "\n", args);
        }
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
        } catch (IOException e) {
            System.out.println("Failed to clear screen");
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

    public static void showAccountInfoMenu(String name, double balance, double winRate, int wins, int loses) {
        clearScreen();
        displayMenu(MenuType.ACCOUNT_INFO_MENU, name, balance, winRate, wins, loses);
    }

    public static void showGameActionsMenu() {
        displayMenu(MenuType.GAME_ACTIONS_MENU);
    }

    public static void showMapMenu() {
        clearScreen();
        displayMenu(MenuType.MAP_MENU);
    }

    public static void invalidOptionText() {
        displayMenu(MenuType.INVALID_OPTION_TEXT);
    }

    private static final String MAIN_MENU = """
            \u001B[36m\u001B[1m╟►────────────────────────────────◄═══════════[[████]]══════════►─────────────────────────────────────◄╣\u001B[0m

                            \u001B[33m\u001B[1m███╗   ███╗ █████╗ ██╗███╗   ██╗    ███╗   ███╗███████╗███╗   ██╗██╗   ██╗
                            ████╗ ████║██╔══██╗██║████╗  ██║    ████╗ ████║██╔════╝████╗  ██║██║   ██║
                            ██╔████╔██║███████║██║██╔██╗ ██║    ██╔████╔██║█████╗  ██╔██╗ ██║██║   ██║
                            ██║╚██╔╝██║██╔══██║██║██║╚██╗██║    ██║╚██╔╝██║██╔══╝  ██║╚██╗██║██║   ██║
                            ██║ ╚═╝ ██║██║  ██║██║██║ ╚████║    ██║ ╚═╝ ██║███████╗██║ ╚████║╚██████╔╝
                            ╚═╝     ╚═╝╚═╝  ╚═╝╚═╝╚═╝  ╚═══╝    ╚═╝     ╚═╝╚══════╝╚═╝  ╚═══╝ ╚═════╝\u001B[0m

              \u001B[32mStart Game (S)\u001B[0m
              \u001B[34mAccount (A)\u001B[0m
              \u001B[36mHelp (H)\u001B[0m
              \u001B[31mExit (X)\u001B[0m

            \u001B[36m\u001B[1m╟►────────────────────────────────◄═══════════[[████]]══════════►─────────────────────────────────────◄╣\u001B[0m
            """;

    private static final String HELP_MENU = """
            \u001B[36m\u001B[1m╟►────────────────────────────────◄═══════════[[████]]══════════►─────────────────────────────────────◄╣\u001B[0m

             \u001B[33m\u001B[1m▀█████████▄   ▄█          ▄████████  ▄████████    ▄█   ▄█▄      ▄█    ▄████████ ▄████████    ▄█   ▄█▄
               ███    ███ ███         ███    ███ ███    ███   ███ ▄███▀     ███   ███    ███ ███    ███   ███ ▄███▀
               ███    ███ ███         ███    ███ ███    █▀    ███▐██▀       ███   ███    ███ ███    █▀    ███▐██▀
              ▄███▄▄▄██▀  ███         ███    ███ ███         ▄█████▀        ███   ███    ███ ███         ▄█████▀
             ▀▀███▀▀▀██▄  ███       ▀███████████ ███        ▀▀█████▄        ███ ▀███████████ ███        ▀▀█████▄
               ███    ██▄ ███         ███    ███ ███    █▄    ███▐██▄       ███   ███    ███ ███    █▄    ███▐██▄
               ███    ███ ███▌    ▄   ███    ███ ███    ███   ███ ▀███▄     ███   ███    ███ ███    ███   ███ ▀███▄
             ▄█████████▀  █████▄▄██   ███    █▀  ████████▀    ███   ▀█▀ █▄ ▄███   ███    █▀  ████████▀    ███   ▀█▀\u001B[0m

            \u001B[37m\u001B[1m  Navigate through 6 map nodes + a final boss battle.
              Each node has 3 choices: 1 Dealer ⚜ and 2 Hidden ???

              \u001B[33mDealers:\u001B[0m
              \u001B[37m  • Standard: Fair play, normal rules
              • Rogue: Will cheat — swaps cards for better ones
              • Advantaged: Higher blackjack chance, stands on 18

              \u001B[33mLives:\u001B[0m \u001B[37m♥♥♥ 3/3 — Lose a hand, lose a life. Reset each node.

              \u001B[33mHidden:\u001B[0m \u001B[37mCould be: Dealer, Cashout, Lose Life, or Debuff.
              \u001B[33mCashout:\u001B[0m \u001B[37mRemoves debuffs + grants a buff. Only found inside hidden nodes.
              \u001B[33mBoss (Round 7):\u001B[0m \u001B[37mThe House — combines all dealer abilities.\u001B[0m

              \u001B[31mBack (X)\u001B[0m

            \u001B[36m\u001B[1m╟►────────────────────────────────◄═══════════[[████]]══════════►─────────────────────────────────────◄╣\u001B[0m
            """;

    private static final String USER_ACCESS_MENU = """
            \u001B[36m\u001B[1m╟►────────────────────────────────◄═══════════[[████]]══════════►─────────────────────────────────────◄╣\u001B[0m
              \u001B[32mSign Up (S)\u001B[0m
              \u001B[34mLogin (L)\u001B[0m
              \u001B[33mGuest (G)\u001B[0m
              \u001B[31mExit (X)\u001B[0m
            \u001B[36m\u001B[1m╟►────────────────────────────────◄═══════════[[████]]══════════►─────────────────────────────────────◄╣\u001B[0m
            """;

    private static final String DEALER_TOP_MENU = "═══════════════════════════════════════════════════════════════════════════════";

    private static final String PLAYER_BOTTOM_MENU = "═══════════════════════════════════════════════════════════════════════════════";

    private static final String ACCOUNT_INFO_MENU = """
            \u001B[36m\u001B[1m╟►────────────────────────────────◄═══════════[[████]]══════════►─────────────────────────────────────◄╣\u001B[0m
              \u001B[33mName:\u001B[0m %s
              \u001B[33mGemstones:\u001B[0m %.2f
              \u001B[33mWin Rate:\u001B[0m %.0f%%
              \u001B[33mWins:\u001B[0m %d
              \u001B[33mLosses:\u001B[0m %d
              \u001B[31mBack (X)\u001B[0m
            \u001B[36m\u001B[1m╟►────────────────────────────────◄═══════════[[████]]══════════►─────────────────────────────────────◄╣\u001B[0m
            """;

    private static final String GAME_ACTIONS_MENU = "Hit (H) | Stand (S) | ";

    private static final String MAP_MENU = "Available Encounters: ";

    private static final String SKILL_SELECTION_MENU = "Choose your skill: ";

    private static final String DECK_SELECTION_MENU = "Choose your deck: ";

    private static final String INVALID_OPTION_TEXT = "\u001B[31mInvalid option. Try again.\u001B[0m";
}
