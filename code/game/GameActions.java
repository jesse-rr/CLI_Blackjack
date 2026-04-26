package code.game;

import code.models.Card;
import code.models.Hand;
import code.models.Player;
import code.models.dealer.*;
import code.models.enums.*;
import code.models.systems.GameMap;
import code.models.systems.MapNode;
import code.ui.CardDisplay;
import code.ui.MapDisplay;
import code.ui.MenuManagement;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class GameActions {
    private static final int SLEEP_SHORT = 1000;
    private static final int SLEEP_LONG = 2000;
    private static final int SLEEP_CARD = 1200;
    private static final Random random = new Random();
    private static final int WINS_TO_BEAT_DEALER = 2;
    private static final int WINS_TO_BEAT_BOSS = 3;

    public static void startGame(Player player) throws InterruptedException {
        player.resetForNewGame();
        MenuManagement.clearScreen();

        SkillType chosenSkill = chooseSkill();
        player.setChosenSkill(chosenSkill);

        DeckTheme chosenDeck = chooseDeck();

        MenuManagement.clearScreen();
        System.out.println(MenuManagement.BOLD + MenuManagement.CYAN + "\n    ═══════════════════════════════════" + MenuManagement.RESET);
        System.out.println(MenuManagement.BOLD + MenuManagement.GREEN + "      The game begins..." + MenuManagement.RESET);
        System.out.println(MenuManagement.BOLD + MenuManagement.YELLOW + "      Skill: " + getSkillName(chosenSkill) + MenuManagement.RESET);
        System.out.println(MenuManagement.BOLD + MenuManagement.YELLOW + "      Deck:  " + getDeckName(chosenDeck) + MenuManagement.RESET);
        System.out.println(MenuManagement.BOLD + MenuManagement.CYAN + "    ═══════════════════════════════════\n" + MenuManagement.RESET);
        Thread.sleep(SLEEP_LONG);

        GameMap map = player.getGameMap();

        for (int round = 1; round <= map.getTotalRounds(); round++) {
            player.resetLives();
            MenuManagement.clearScreen();
            MapDisplay.displayMap(map, round);
            System.out.println(MenuManagement.BOLD + "    " + player.getLivesDisplay() + MenuManagement.RESET);
            displayActiveEffects(player);

            MapNode chosenNode = chooseNode(map, round);
            chosenNode.markAsVisited();
            map.advanceRound();

            boolean survived = handleNode(player, chosenNode, chosenDeck);
            if (!survived) {
                MenuManagement.clearScreen();
                System.out.println(MenuManagement.BOLD + MenuManagement.RED);
                System.out.println("    ╔═══════════════════════════════════╗");
                System.out.println("    ║         G A M E   O V E R         ║");
                System.out.println("    ║     You ran out of lives...       ║");
                System.out.println("    ╚═══════════════════════════════════╝");
                System.out.println(MenuManagement.RESET);
                Thread.sleep(SLEEP_LONG);
                return;
            }
        }

        player.resetLives();
        MenuManagement.clearScreen();
        MapDisplay.displayMap(map, map.getTotalRounds() + 1);
        System.out.println(MenuManagement.BOLD + "    " + player.getLivesDisplay() + MenuManagement.RESET);
        displayActiveEffects(player);
        System.out.println(MenuManagement.BOLD + MenuManagement.RED + "\n    ⚔  THE FINAL BOSS AWAITS  ⚔" + MenuManagement.RESET);
        System.out.println(MenuManagement.BOLD + MenuManagement.RED + "    Win " + WINS_TO_BEAT_BOSS + " hands to defeat The House!" + MenuManagement.RESET);
        Thread.sleep(SLEEP_LONG);

        MapNode bossNode = map.getBossNode();
        bossNode.markAsVisited();
        boolean bossWin = playDealerNode(player, new BossDealer("The House"), chosenDeck, WINS_TO_BEAT_BOSS);

        MenuManagement.clearScreen();
        if (bossWin) {
            System.out.println(MenuManagement.BOLD + MenuManagement.GREEN);
            System.out.println("    ╔═══════════════════════════════════════════╗");
            System.out.println("    ║                                           ║");
            System.out.println("    ║     ★  Y O U   D E F E A T E D   ★        ║");
            System.out.println("    ║         T H E   H O U S E                 ║");
            System.out.println("    ║                                           ║");
            System.out.println("    ║        Congratulations, Champion!         ║");
            System.out.println("    ║                                           ║");
            System.out.println("    ╚═══════════════════════════════════════════╝");
            System.out.println(MenuManagement.RESET);
        } else {
            System.out.println(MenuManagement.BOLD + MenuManagement.RED);
            System.out.println("    ╔═══════════════════════════════════════════╗");
            System.out.println("    ║                                           ║");
            System.out.println("    ║         T H E   H O U S E   W I N S       ║");
            System.out.println("    ║                                           ║");
            System.out.println("    ║         Better luck next time...          ║");
            System.out.println("    ║                                           ║");
            System.out.println("    ╚═══════════════════════════════════════════╝");
            System.out.println(MenuManagement.RESET);
        }
        Thread.sleep(SLEEP_LONG);
    }

    private static SkillType chooseSkill() {
        Scanner scanner = new Scanner(System.in);
        MenuManagement.clearScreen();
        System.out.println(MenuManagement.BOLD + MenuManagement.CYAN);
        System.out.println("    ╔═══════════════════════════════════════════╗");
        System.out.println("    ║          C H O O S E   S K I L L          ║");
        System.out.println("    ╠═══════════════════════════════════════════╣");
        System.out.println(MenuManagement.RESET);
        System.out.println(MenuManagement.YELLOW + "    ║  [1] " + MenuManagement.BOLD + "Card Counting" + MenuManagement.RESET);
        System.out.println(MenuManagement.WHITE + "    ║      Peek at the next card in the deck." + MenuManagement.RESET);
        System.out.println(MenuManagement.WHITE + "    ║      Uses: 3 per game" + MenuManagement.RESET);
        System.out.println("    ║");
        System.out.println(MenuManagement.YELLOW + "    ║  [2] " + MenuManagement.BOLD + "Rigged Shuffle" + MenuManagement.RESET);
        System.out.println(MenuManagement.WHITE + "    ║      Force the dealer to reshuffle the deck." + MenuManagement.RESET);
        System.out.println(MenuManagement.WHITE + "    ║      Uses: 3 per game" + MenuManagement.RESET);
        System.out.println("    ║");
        System.out.println(MenuManagement.YELLOW + "    ║  [3] " + MenuManagement.BOLD + "Fortune's Favor" + MenuManagement.RESET);
        System.out.println(MenuManagement.WHITE + "    ║      Redraw your last drawn card." + MenuManagement.RESET);
        System.out.println(MenuManagement.WHITE + "    ║      Uses: 3 per game" + MenuManagement.RESET);
        System.out.println(MenuManagement.CYAN);
        System.out.println("    ╚═══════════════════════════════════════════╝");
        System.out.println(MenuManagement.RESET);
        System.out.print("    Choose [1/2/3]: ");

        while (true) {
            String input = scanner.next().trim();
            switch (input) {
                case "1": return SkillType.CARD_COUNTING;
                case "2": return SkillType.RIGGED_SHUFFLE;
                case "3": return SkillType.FORTUNES_FAVORITE;
                default:
                    System.out.print("    Invalid. Choose [1/2/3]: ");
            }
        }
    }

    private static DeckTheme chooseDeck() {
        Scanner scanner = new Scanner(System.in);
        MenuManagement.clearScreen();
        System.out.println(MenuManagement.BOLD + MenuManagement.MAGENTA);
        System.out.println("    ╔═══════════════════════════════════════════╗");
        System.out.println("    ║           C H O O S E   D E C K           ║");
        System.out.println("    ╠═══════════════════════════════════════════╣");
        System.out.println(MenuManagement.RESET);
        System.out.println(MenuManagement.GREEN + "    ║  [1] " + MenuManagement.BOLD + "Standard Deck" + MenuManagement.RESET);
        System.out.println(MenuManagement.WHITE + "    ║      Classic 52-card deck. No surprises." + MenuManagement.RESET);
        System.out.println("    ║");
        System.out.println(MenuManagement.RED + "    ║  [2] " + MenuManagement.BOLD + "Corrupted Deck" + MenuManagement.RESET);
        System.out.println(MenuManagement.WHITE + "    ║      50% of cards are corrupted (§) and" + MenuManagement.RESET);
        System.out.println(MenuManagement.WHITE + "    ║      do NOT count toward your hand total." + MenuManagement.RESET);
        System.out.println(MenuManagement.WHITE + "    ║      High chaos, unpredictable draws." + MenuManagement.RESET);
        System.out.println("    ║");
        System.out.println(MenuManagement.YELLOW + "    ║  [3] " + MenuManagement.BOLD + "Fool's Deck" + MenuManagement.RESET);
        System.out.println(MenuManagement.WHITE + "    ║      Contains ★ Wild Jokers (25% of deck)." + MenuManagement.RESET);
        System.out.println(MenuManagement.WHITE + "    ║      YOU choose each Joker's value (1-11)." + MenuManagement.RESET);
        System.out.println(MenuManagement.WHITE + "    ║      High risk, high reward." + MenuManagement.RESET);
        System.out.println(MenuManagement.MAGENTA);
        System.out.println("    ╚═══════════════════════════════════════════╝");
        System.out.println(MenuManagement.RESET);
        System.out.print("    Choose [1/2/3]: ");

        while (true) {
            String input = scanner.next().trim();
            switch (input) {
                case "1": return DeckTheme.STANDARD;
                case "2": return DeckTheme.CORRUPTED;
                case "3": return DeckTheme.FOOLS;
                default:
                    System.out.print("    Invalid. Choose [1/2/3]: ");
            }
        }
    }

    private static MapNode chooseNode(GameMap map, int round) {
        Scanner scanner = new Scanner(System.in);
        List<MapNode> nodes = map.getNodesForRound(round);

        System.out.println(MenuManagement.BOLD + MenuManagement.YELLOW + "\n    Choose a node:" + MenuManagement.RESET);
        for (int i = 0; i < nodes.size(); i++) {
            MapNode n = nodes.get(i);
            String symbol = getNodeSymbol(n.getType());
            System.out.println("      [" + (i + 1) + "] " + symbol + " " + n.getNodeName());
        }
        System.out.print("\n    Choose [1/2/3]: ");

        while (true) {
            String input = scanner.next().trim();
            try {
                int choice = Integer.parseInt(input);
                if (choice >= 1 && choice <= 3) {
                    return nodes.get(choice - 1);
                }
            } catch (NumberFormatException ignored) {}
            System.out.print("    Invalid. Choose [1/2/3]: ");
        }
    }

    private static boolean handleNode(Player player, MapNode node, DeckTheme deckTheme) throws InterruptedException {
        return switch (node.getType()) {
            case DEALER -> handleDealerNode(player, deckTheme);
            case CASHOUT -> handleCashoutNode(player);
            case HIDDEN -> handleHiddenNode(player, node, deckTheme);
            case BOSS -> playDealerNode(player, new BossDealer("The House"), deckTheme, WINS_TO_BEAT_BOSS);
        };
    }

    private static boolean handleDealerNode(Player player, DeckTheme deckTheme) throws InterruptedException {
        Dealer dealer = createRandomDealer();
        MenuManagement.clearScreen();
        System.out.println(MenuManagement.BOLD + MenuManagement.CYAN);
        System.out.println("    ╔═══════════════════════════════════════════╗");
        String headerText = dealer.getTitle() + ": " + dealer.getName();
        System.out.printf("    ║  %-40s ║%n", headerText);
        System.out.println("    ║  Win " + WINS_TO_BEAT_DEALER + " hands to defeat this dealer!       ║");
        System.out.println("    ╚═══════════════════════════════════════════╝");
        System.out.println(MenuManagement.RESET);
        Thread.sleep(SLEEP_SHORT);
        return playDealerNode(player, dealer, deckTheme, WINS_TO_BEAT_DEALER);
    }

    private static boolean handleCashoutNode(Player player) throws InterruptedException {
        MenuManagement.clearScreen();
        player.clearAllDebuffs();

        BuffType buff = getRandomBuff();
        player.addBuff(buff);

        System.out.println(MenuManagement.BOLD + MenuManagement.GREEN);
        System.out.println("    ╔═══════════════════════════════════════════╗");
        System.out.println("    ║            C A S H O U T                  ║");
        System.out.println("    ╠═══════════════════════════════════════════╣");
        System.out.println("    ║                                           ║");
        System.out.println("    ║   ✓  All debuffs have been removed!       ║");
        System.out.println("    ║                                           ║");
        System.out.printf("    ║  ★  Buff gained: %-25s║%n", getBuffName(buff));
        System.out.printf("    ║     %-38s║%n", getBuffDescription(buff));
        System.out.println("    ║                                           ║");
        System.out.println("    ╚═══════════════════════════════════════════╝");
        System.out.println(MenuManagement.RESET);

        Thread.sleep(SLEEP_LONG);
        waitForContinue();
        return true;
    }

    private static boolean handleHiddenNode(Player player, MapNode node, DeckTheme deckTheme) throws InterruptedException {
        MenuManagement.clearScreen();
        HiddenNodeOutcome outcome = node.getHiddenOutcome();

        System.out.println(MenuManagement.BOLD + MenuManagement.MAGENTA);
        System.out.println("    ╔═══════════════════════════════════╗");
        System.out.println("    ║           ? ? ?   N O D E         ║");
        System.out.println("    ║         Revealing outcome...      ║");
        System.out.println("    ╚═══════════════════════════════════╝");
        System.out.println(MenuManagement.RESET);
        Thread.sleep(SLEEP_LONG);

        return switch (outcome) {
            case LOSE_LIFE -> {
                player.getLivesSystem().loseLife();
                MenuManagement.clearScreen();
                System.out.println(MenuManagement.BOLD + MenuManagement.RED);
                System.out.println("    ╔═══════════════════════════════════╗");
                System.out.println("    ║         ✗  TRAP!                  ║");
                System.out.println("    ║     You lost a life!              ║");
                System.out.println("    ║     " + player.getLivesDisplay() + "                       ║");
                System.out.println("    ╚═══════════════════════════════════╝");
                System.out.println(MenuManagement.RESET);
                Thread.sleep(SLEEP_LONG);
                waitForContinue();
                yield player.getLivesSystem().isAlive();
            }
            case DEALER -> {
                MenuManagement.clearScreen();
                System.out.println(MenuManagement.BOLD + MenuManagement.YELLOW);
                System.out.println("    ╔═══════════════════════════════════╗");
                System.out.println("    ║    ⚜  Hidden Dealer revealed!     ║");
                System.out.println("    ╚═══════════════════════════════════╝");
                System.out.println(MenuManagement.RESET);
                Thread.sleep(SLEEP_SHORT);
                yield handleDealerNode(player, deckTheme);
            }
            case CASHOUT -> {
                MenuManagement.clearScreen();
                System.out.println(MenuManagement.BOLD + MenuManagement.GREEN);
                System.out.println("    ╔═══════════════════════════════════╗");
                System.out.println("    ║    ★  Hidden Cashout revealed!    ║");
                System.out.println("    ╚═══════════════════════════════════╝");
                System.out.println(MenuManagement.RESET);
                Thread.sleep(SLEEP_SHORT);
                yield handleCashoutNode(player);
            }
            case DEBUFF -> {
                DebuffType debuff = getRandomDebuff();
                player.addDebuff(debuff);
                MenuManagement.clearScreen();
                System.out.println(MenuManagement.BOLD + MenuManagement.RED);
                System.out.println("    ╔═══════════════════════════════════════════╗");
                System.out.println("    ║            ✗  C U R S E D                 ║");
                System.out.println("    ╠═══════════════════════════════════════════╣");
                System.out.printf("    ║  ✗  Debuff: %-30s║%n", getDebuffName(debuff));
                System.out.printf("    ║     %-38s║%n", getDebuffDescription(debuff));
                System.out.println("    ║                                           ║");
                System.out.println("    ╚═══════════════════════════════════════════╝");
                System.out.println(MenuManagement.RESET);
                Thread.sleep(SLEEP_LONG);
                waitForContinue();
                yield true;
            }
        };
    }

    private static boolean playDealerNode(Player player, Dealer dealer, DeckTheme deckTheme, int winsNeeded) throws InterruptedException {
        TableActions table = new TableActions();
        table.initializeDeck(deckTheme, 1);
        Scanner scanner = new Scanner(System.in);
        int playerWins = 0;
        int handNumber = 0;

        while (player.getLivesSystem().isAlive() && playerWins < winsNeeded) {
            handNumber++;
            Hand playerHand = new Hand();
            dealer.resetHand();
            Hand dealerHand = dealer.getHand();

            MenuManagement.clearScreen();
            System.out.println(MenuManagement.BOLD + MenuManagement.CYAN + "    ═══════════════════════════════════════" + MenuManagement.RESET);
            System.out.println(MenuManagement.BOLD + "    " + dealer.getTitle() + ": " + dealer.getName() + MenuManagement.RESET);
            System.out.println("    " + player.getLivesDisplay());
            System.out.println(MenuManagement.BOLD + MenuManagement.YELLOW + "    Hand #" + handNumber + "  │  Wins: " + playerWins + "/" + winsNeeded + MenuManagement.RESET);
            if (player.getSkillUsesRemaining() > 0) {
                System.out.println("    Skill: " + getSkillName(player.getChosenSkill()) + " [" + player.getSkillUsesRemaining() + " uses left]");
            }
            displayActiveEffects(player);
            System.out.println(MenuManagement.BOLD + MenuManagement.CYAN + "    ═══════════════════════════════════════" + MenuManagement.RESET);
            Thread.sleep(SLEEP_SHORT);

            System.out.println(MenuManagement.BOLD + MenuManagement.YELLOW + "\n    Dealing cards..." + MenuManagement.RESET);
            Thread.sleep(SLEEP_CARD);

            System.out.println(MenuManagement.CYAN + "    ► Drawing your first card..." + MenuManagement.RESET);
            Thread.sleep(SLEEP_CARD);
            Card pc1 = table.drawCard();
            playerHand.addCard(pc1);
            handleSpecialCard(pc1, playerHand, scanner, player);
            System.out.println(MenuManagement.WHITE + "      → You drew: " + pc1.display() + MenuManagement.RESET);
            CardDisplay.displaySingleCard(pc1.display());
            Thread.sleep(SLEEP_CARD);

            System.out.println(MenuManagement.RED + "    ► Dealer draws face-up card..." + MenuManagement.RESET);
            Thread.sleep(SLEEP_CARD);
            Card dc1 = drawDealerCard(table, dealer, dealerHand);
            dealerHand.addCard(dc1);
            System.out.println(MenuManagement.WHITE + "      → Dealer shows: " + dc1.display() + MenuManagement.RESET);
            CardDisplay.displaySingleCard(dc1.display());
            Thread.sleep(SLEEP_CARD);

            System.out.println(MenuManagement.CYAN + "    ► Drawing your second card..." + MenuManagement.RESET);
            Thread.sleep(SLEEP_CARD);
            Card pc2 = table.drawCard();
            playerHand.addCard(pc2);
            handleSpecialCard(pc2, playerHand, scanner, player);
            System.out.println(MenuManagement.WHITE + "      → You drew: " + pc2.display() + MenuManagement.RESET);
            CardDisplay.displaySingleCard(pc2.display());
            Thread.sleep(SLEEP_CARD);

            System.out.println(MenuManagement.RED + "    ► Dealer draws face-down card..." + MenuManagement.RESET);
            Thread.sleep(SLEEP_CARD);
            Card dc2 = drawDealerCard(table, dealer, dealerHand);
            dealerHand.addCard(dc2);
            System.out.println(MenuManagement.WHITE + "      → Dealer's card is hidden" + MenuManagement.RESET);
            CardDisplay.displayFaceDownCard();
            Thread.sleep(SLEEP_CARD);

            if (dealer.getBlackjackBonus() > 0 && random.nextDouble() < dealer.getBlackjackBonus()) {
                dealerHand.clear();
                dealerHand.addCard(new Card("A", "♠"));
                String[] tens = {"10", "J", "Q", "K"};
                dealerHand.addCard(new Card(tens[random.nextInt(tens.length)], "♥"));
            }

            MenuManagement.clearScreen();
            printHandHeader(dealer, player, playerWins, winsNeeded, handNumber);

            boolean showDealerCard = player.hasBuff(BuffType.DEALER_REVEALS_CARD);
            displayHands(playerHand, dealerHand, showDealerCard, player);

            if (table.isBlackjack(playerHand)) {
                System.out.println(MenuManagement.BOLD + MenuManagement.GREEN + "\n    ★ BLACKJACK! You win this hand! ★" + MenuManagement.RESET);
                player.addWin();
                playerWins++;
                System.out.println(MenuManagement.BOLD + MenuManagement.YELLOW + "    Score: " + playerWins + "/" + winsNeeded + " wins" + MenuManagement.RESET);
                Thread.sleep(SLEEP_LONG);
                if (playerWins >= winsNeeded) break;
                waitForContinue();
                continue;
            }

            if (table.isBlackjack(dealerHand)) {
                System.out.println(MenuManagement.BOLD + MenuManagement.RED + "\n    Dealer has BLACKJACK! You lose this hand." + MenuManagement.RESET);
                player.addLoss();
                System.out.println("    " + player.getLivesDisplay());
                System.out.println(MenuManagement.BOLD + MenuManagement.YELLOW + "    Score: " + playerWins + "/" + winsNeeded + " wins" + MenuManagement.RESET);
                Thread.sleep(SLEEP_LONG);
                if (!player.getLivesSystem().isAlive()) return false;
                waitForContinue();
                continue;
            }

            boolean playerTurnDone = false;
            boolean playerBusted = false;
            boolean usedIronHand = false;

            while (!playerTurnDone) {
                int playerValue = player.hasDebuff(DebuffType.HEAVY_CARDS) ? playerHand.getValueWithHeavyCards() : playerHand.getValue();

                System.out.print(MenuManagement.BOLD + "\n    [H]it  [S]tand");
                if (player.getSkillUsesRemaining() > 0) {
                    System.out.print("  [U]se Skill");
                }
                System.out.println(MenuManagement.RESET);
                System.out.print("    > ");

                String action = scanner.next().trim().toLowerCase();

                switch (action) {
                    case "h" -> {
                        System.out.println(MenuManagement.CYAN + "\n    ► Drawing a card for you..." + MenuManagement.RESET);
                        Thread.sleep(SLEEP_CARD);
                        Card drawn = table.drawCard();
                        playerHand.addCard(drawn);
                        handleSpecialCard(drawn, playerHand, scanner, player);
                        System.out.println(MenuManagement.WHITE + "      → You drew: " + drawn.display() + MenuManagement.RESET);
                        CardDisplay.displaySingleCard(drawn.display());
                        Thread.sleep(SLEEP_CARD);

                        playerValue = player.hasDebuff(DebuffType.HEAVY_CARDS) ? playerHand.getValueWithHeavyCards() : playerHand.getValue();

                        MenuManagement.clearScreen();
                        printHandHeader(dealer, player, playerWins, winsNeeded, handNumber);
                        displayHands(playerHand, dealerHand, showDealerCard, player);

                        if (playerValue > 21) {
                            if (player.hasBuff(BuffType.IRON_HAND) && !usedIronHand) {
                                usedIronHand = true;
                                System.out.println(MenuManagement.BOLD + MenuManagement.YELLOW + "\n    ⛊ Iron Hand saved you from busting!" + MenuManagement.RESET);
                                player.removeBuff(BuffType.IRON_HAND);
                                playerTurnDone = true;
                            } else {
                                System.out.println(MenuManagement.BOLD + MenuManagement.RED + "\n    BUST! You went over 21." + MenuManagement.RESET);
                                playerBusted = true;
                                playerTurnDone = true;
                            }
                        }

                        int crackedMax = player.hasDebuff(DebuffType.CRACKED_HAND) ? 19 : 21;
                        if (playerValue >= crackedMax && !playerBusted) {
                            if (playerValue >= crackedMax) {
                                System.out.println(MenuManagement.YELLOW + "    Auto-standing at " + playerValue + "." + MenuManagement.RESET);
                            }
                            playerTurnDone = true;
                        }
                    }
                    case "s" -> {
                        int minStand = player.hasDebuff(DebuffType.SHAKY_FINGERS) ? 15 : 0;
                        if (playerValue < minStand) {
                            System.out.println(MenuManagement.RED + "    Shaky Fingers! You can't stand below " + minStand + "." + MenuManagement.RESET);
                        } else {
                            System.out.println(MenuManagement.CYAN + "    You stand at " + playerValue + "." + MenuManagement.RESET);
                            Thread.sleep(SLEEP_CARD);
                            playerTurnDone = true;
                        }
                    }
                    case "u" -> {
                        if (player.getSkillUsesRemaining() > 0) {
                            useSkill(player, table, playerHand, scanner);
                            MenuManagement.clearScreen();
                            printHandHeader(dealer, player, playerWins, winsNeeded, handNumber);
                            displayHands(playerHand, dealerHand, showDealerCard, player);
                        } else {
                            System.out.println(MenuManagement.RED + "    No skill uses remaining." + MenuManagement.RESET);
                        }
                    }
                    default -> System.out.println(MenuManagement.RED + "    Invalid action." + MenuManagement.RESET);
                }
            }

            if (playerBusted) {
                player.addLoss();
                System.out.println("    " + player.getLivesDisplay());
                System.out.println(MenuManagement.BOLD + MenuManagement.YELLOW + "    Score: " + playerWins + "/" + winsNeeded + " wins" + MenuManagement.RESET);
                Thread.sleep(SLEEP_LONG);
                if (!player.getLivesSystem().isAlive()) return false;
                waitForContinue();
                continue;
            }

            int dealerThreshold = dealer.getHitThreshold();
            if (player.hasDebuff(DebuffType.DEALERS_MARK)) {
                dealerThreshold = Math.max(dealerThreshold - 1, 16);
            }

            System.out.println(MenuManagement.BOLD + MenuManagement.CYAN + "\n    ═══ Dealer's Turn ═══" + MenuManagement.RESET);
            System.out.println(MenuManagement.BOLD + MenuManagement.RED + "    Dealer reveals hidden card: " + dealerHand.getCards().get(1).display() + MenuManagement.RESET);
            System.out.println(MenuManagement.WHITE + "    Dealer's total: " + dealerHand.getValue() + MenuManagement.RESET);
            Thread.sleep(SLEEP_SHORT);

            while (table.shouldDealerHit(dealerHand.getValue(), dealerThreshold)) {
                System.out.println(MenuManagement.RED + "\n    ► Dealer draws a card..." + MenuManagement.RESET);
                Thread.sleep(SLEEP_CARD);
                Card dealerDraw = drawDealerCard(table, dealer, dealerHand);
                dealerHand.addCard(dealerDraw);
                System.out.println(MenuManagement.WHITE + "      → Dealer drew: " + dealerDraw.display() + MenuManagement.RESET);
                CardDisplay.displaySingleCard(dealerDraw.display());
                System.out.println(MenuManagement.WHITE + "      → Dealer's total: " + dealerHand.getValue() + MenuManagement.RESET);
                Thread.sleep(SLEEP_CARD);
            }

            if (!table.isBust(dealerHand.getValue())) {
                System.out.println(MenuManagement.RED + "\n    Dealer stands at " + dealerHand.getValue() + "." + MenuManagement.RESET);
            }
            Thread.sleep(SLEEP_CARD);

            MenuManagement.clearScreen();
            printHandHeader(dealer, player, playerWins, winsNeeded, handNumber);

            System.out.println(MenuManagement.BOLD + "\n    ═══ FINAL RESULTS ═══" + MenuManagement.RESET);

            System.out.println(MenuManagement.BOLD + "\n    DEALER'S HAND:" + MenuManagement.RESET);
            List<String> dealerCardStrings = dealerHand.getCards().stream()
                .filter(c -> !c.isCorrupted())
                .map(Card::display)
                .toList();
            CardDisplay.displayCards(dealerCardStrings, dealerCardStrings.size(), false, dealerHand.getValue());

            System.out.println(MenuManagement.BOLD + "    YOUR HAND:" + MenuManagement.RESET);
            int displayValue = player.hasDebuff(DebuffType.HEAVY_CARDS) ? playerHand.getValueWithHeavyCards() : playerHand.getValue();
            List<String> playerCardStrings = playerHand.getCards().stream()
                .filter(c -> !c.isCorrupted())
                .map(c -> {
                    if (player.hasDebuff(DebuffType.BLIND_DRAW)) return "??";
                    return c.display();
                }).toList();
            CardDisplay.displayCards(playerCardStrings, playerCardStrings.size(), false, displayValue);

            int finalPlayerValue = player.hasDebuff(DebuffType.HEAVY_CARDS) ? playerHand.getValueWithHeavyCards() : playerHand.getValue();
            int finalDealerValue = dealerHand.getValue();

            if (table.isBust(finalDealerValue)) {
                System.out.println(MenuManagement.BOLD + MenuManagement.GREEN + "\n    Dealer BUSTS at " + finalDealerValue + "! You win! ★" + MenuManagement.RESET);
                player.addWin();
                playerWins++;
            } else if (finalPlayerValue > finalDealerValue) {
                System.out.println(MenuManagement.BOLD + MenuManagement.GREEN + "\n    You win! " + finalPlayerValue + " vs " + finalDealerValue + " ★" + MenuManagement.RESET);
                player.addWin();
                playerWins++;
            } else if (finalPlayerValue < finalDealerValue) {
                System.out.println(MenuManagement.BOLD + MenuManagement.RED + "\n    Dealer wins. " + finalPlayerValue + " vs " + finalDealerValue + MenuManagement.RESET);
                player.addLoss();
                System.out.println("    " + player.getLivesDisplay());
            } else {
                System.out.println(MenuManagement.BOLD + MenuManagement.YELLOW + "\n    Push! It's a tie. " + finalPlayerValue + " vs " + finalDealerValue + MenuManagement.RESET);
            }

            System.out.println(MenuManagement.BOLD + MenuManagement.YELLOW + "    Score: " + playerWins + "/" + winsNeeded + " wins" + MenuManagement.RESET);

            Thread.sleep(SLEEP_LONG);

            if (!player.getLivesSystem().isAlive()) return false;
            if (playerWins >= winsNeeded) break;

            waitForContinue();
        }

        if (playerWins >= winsNeeded) {
            System.out.println(MenuManagement.BOLD + MenuManagement.GREEN + "\n    ════════════════════════════════════" + MenuManagement.RESET);
            System.out.println(MenuManagement.BOLD + MenuManagement.GREEN + "    ★  DEALER DEFEATED!  ★" + MenuManagement.RESET);
            System.out.println(MenuManagement.BOLD + MenuManagement.GREEN + "    ════════════════════════════════════" + MenuManagement.RESET);
            Thread.sleep(SLEEP_LONG);
            waitForContinue();
            return true;
        }

        return false;
    }

    private static void printHandHeader(Dealer dealer, Player player, int wins, int winsNeeded, int handNumber) {
        System.out.println(MenuManagement.BOLD + MenuManagement.CYAN + "    ═══════════════════════════════════════" + MenuManagement.RESET);
        System.out.println(MenuManagement.BOLD + "    " + dealer.getTitle() + ": " + dealer.getName() + MenuManagement.RESET);
        System.out.println("    " + player.getLivesDisplay());
        System.out.println(MenuManagement.BOLD + MenuManagement.YELLOW + "    Hand #" + handNumber + "  │  Wins: " + wins + "/" + winsNeeded + MenuManagement.RESET);
        if (player.getSkillUsesRemaining() > 0) {
            System.out.println("    Skill: " + getSkillName(player.getChosenSkill()) + " [" + player.getSkillUsesRemaining() + " uses left]");
        }
        displayActiveEffects(player);
        System.out.println(MenuManagement.BOLD + MenuManagement.CYAN + "    ═══════════════════════════════════════" + MenuManagement.RESET);
    }

    private static Card drawDealerCard(TableActions table, Dealer dealer, Hand dealerHand) {
        if (dealer.shouldCheat()) {
            return table.drawCheatCard(dealerHand);
        }
        if (dealer.getType() == DealerType.ADVANTAGED || dealer.getType() == DealerType.BOSS) {
            return table.drawAdvantagedCard(dealerHand);
        }
        return table.drawCard();
    }

    private static void handleSpecialCard(Card card, Hand hand, Scanner scanner, Player player) {
        if (card.isWild()) {
            System.out.print(MenuManagement.BOLD + MenuManagement.YELLOW + "    ★ Wild Joker drawn! Choose value (1-11): " + MenuManagement.RESET);
            while (true) {
                try {
                    int val = Integer.parseInt(scanner.next().trim());
                    if (val >= 1 && val <= 11) {
                        hand.addWildValue(val);
                        System.out.println("      Joker set to " + val);
                        return;
                    }
                } catch (NumberFormatException ignored) {}
                System.out.print("    Invalid. Choose value (1-11): ");
            }
        }

        if (card.isCorrupted()) {
            System.out.println(MenuManagement.RED + "      § Corrupted card — does not count toward total!" + MenuManagement.RESET);
        }
    }

    private static void useSkill(Player player, TableActions table, Hand playerHand, Scanner scanner) throws InterruptedException {
        SkillType skill = player.getChosenSkill();
        player.useSkill();

        switch (skill) {
            case CARD_COUNTING -> {
                Card nextCard = table.getDeck().drawCard();
                System.out.println(MenuManagement.BOLD + MenuManagement.CYAN + "\n    👁 Skill: Card Counting" + MenuManagement.RESET);
                System.out.println(MenuManagement.CYAN + "    Next card in deck: " + nextCard.display() + MenuManagement.RESET);
                CardDisplay.displaySingleCard(nextCard.display());
                System.out.print(MenuManagement.BOLD + MenuManagement.YELLOW + "    Take this card? [Y/N]: " + MenuManagement.RESET);
                while (true) {
                    String choice = scanner.next().trim().toLowerCase();
                    if (choice.equals("y")) {
                        playerHand.addCard(nextCard);
                        handleSpecialCard(nextCard, playerHand, scanner, player);
                        System.out.println(MenuManagement.GREEN + "    Card added to your hand." + MenuManagement.RESET);
                        break;
                    } else if (choice.equals("n")) {
                        System.out.println(MenuManagement.RED + "    Card discarded." + MenuManagement.RESET);
                        player.refundSkill();
                        break;
                    } else {
                        System.out.print(MenuManagement.RED + "    Invalid. [Y/N]: " + MenuManagement.RESET);
                    }
                }
            }
            case RIGGED_SHUFFLE -> {
                table.getDeck().reshuffle(1);
                System.out.println(MenuManagement.BOLD + MenuManagement.CYAN + "\n    🔄 Skill: Rigged Shuffle" + MenuManagement.RESET);
                System.out.println(MenuManagement.CYAN + "    The deck has been completely reshuffled!" + MenuManagement.RESET);
            }
            case FORTUNES_FAVORITE -> {
                if (playerHand.getSize() > 0) {
                    Card lastCard = playerHand.getCards().remove(playerHand.getSize() - 1);
                    System.out.println(MenuManagement.BOLD + MenuManagement.CYAN + "\n    ↩ Skill: Fortune's Favor" + MenuManagement.RESET);
                    System.out.println(MenuManagement.CYAN + "    Returned " + lastCard.display() + " to the deck." + MenuManagement.RESET);
                    Thread.sleep(SLEEP_CARD);
                    System.out.println(MenuManagement.CYAN + "    ► Drawing a new card..." + MenuManagement.RESET);
                    Thread.sleep(SLEEP_CARD);
                    Card newCard = table.drawCard();
                    playerHand.addCard(newCard);
                    handleSpecialCard(newCard, playerHand, scanner, player);
                    System.out.println(MenuManagement.WHITE + "      → Drew: " + newCard.display() + MenuManagement.RESET);
                }
            }
        }

        Thread.sleep(SLEEP_SHORT);
    }

    private static void displayHands(Hand playerHand, Hand dealerHand, boolean showDealerHidden, Player player) {
        System.out.println(MenuManagement.BOLD + "\n    ═══ DEALER'S HAND ═══" + MenuManagement.RESET);
        List<String> dealerCardStrings = dealerHand.getCards().stream().map(Card::display).toList();
        int dealerShow = showDealerHidden ? dealerHand.getValue() : dealerHand.getCards().get(0).getValue();
        CardDisplay.displayCards(dealerCardStrings, dealerCardStrings.size(), !showDealerHidden, dealerShow);

        System.out.println(MenuManagement.BOLD + "    ═══ YOUR HAND ═══" + MenuManagement.RESET);
        int displayValue = player.hasDebuff(DebuffType.HEAVY_CARDS) ? playerHand.getValueWithHeavyCards() : playerHand.getValue();
        List<String> playerCardStrings;
        if (player.hasDebuff(DebuffType.BLIND_DRAW) && playerHand.getSize() >= 1) {
            playerCardStrings = new java.util.ArrayList<>();
            playerCardStrings.add("??");
            for (int i = 1; i < playerHand.getSize(); i++) {
                playerCardStrings.add(playerHand.getCards().get(i).display());
            }
        } else {
            playerCardStrings = playerHand.getCards().stream().map(Card::display).toList();
        }
        CardDisplay.displayCards(playerCardStrings, playerCardStrings.size(), false, displayValue);
    }

    private static void displayActiveEffects(Player player) {
        if (!player.getActiveBuffs().isEmpty()) {
            System.out.print(MenuManagement.GREEN + "    Buffs: ");
            for (BuffType b : player.getActiveBuffs()) {
                System.out.print("[" + getBuffName(b) + "] ");
            }
            System.out.println(MenuManagement.RESET);
        }
        if (!player.getActiveDebuffs().isEmpty()) {
            System.out.print(MenuManagement.RED + "    Debuffs: ");
            for (DebuffType d : player.getActiveDebuffs()) {
                System.out.print("[" + getDebuffName(d) + "] ");
            }
            System.out.println(MenuManagement.RESET);
        }
    }

    private static Dealer createRandomDealer() {
        String[] names = {"Marcus", "Shade", "Vex", "Jinx", "Dagger", "Bones", "Whisper", "Fang", "Trick", "Mortis"};
        String name = names[random.nextInt(names.length)];

        int roll = random.nextInt(3);
        return switch (roll) {
            case 0 -> new Dealer(name, DealerType.STANDARD);
            case 1 -> new RogueDealer(name);
            case 2 -> new AdvantagedDealer(name);
            default -> new Dealer(name, DealerType.STANDARD);
        };
    }

    private static BuffType getRandomBuff() {
        BuffType[] buffs = BuffType.values();
        return buffs[random.nextInt(buffs.length)];
    }

    private static DebuffType getRandomDebuff() {
        DebuffType[] debuffs = DebuffType.values();
        return debuffs[random.nextInt(debuffs.length)];
    }

    private static String getSkillName(SkillType skill) {
        return switch (skill) {
            case CARD_COUNTING -> "Card Counting";
            case RIGGED_SHUFFLE -> "Rigged Shuffle";
            case FORTUNES_FAVORITE -> "Fortune's Favor";
        };
    }

    private static String getDeckName(DeckTheme deck) {
        return switch (deck) {
            case STANDARD -> "Standard Deck";
            case CORRUPTED -> "Corrupted Deck";
            case FOOLS -> "Fool's Deck";
        };
    }

    private static String getNodeSymbol(MapNodeType type) {
        return switch (type) {
            case DEALER -> "⚜";
            case CASHOUT -> "⛃";
            case HIDDEN -> "?";
            case BOSS -> "⚔";
        };
    }

    private static String getBuffName(BuffType buff) {
        return switch (buff) {
            case EXTRA_LIFE -> "Extra Life";
            case DEALER_REVEALS_CARD -> "X-Ray Vision";
            case DOUBLE_DOWN_SHIELD -> "Shield";
            case LUCKY_SEVENS -> "Lucky 7s";
            case IRON_HAND -> "Iron Hand";
        };
    }

    private static String getBuffDescription(BuffType buff) {
        return switch (buff) {
            case EXTRA_LIFE -> "Gain an extra life for the next node.";
            case DEALER_REVEALS_CARD -> "See the dealer's hidden card.";
            case DOUBLE_DOWN_SHIELD -> "Protection on risky plays.";
            case LUCKY_SEVENS -> "7s bring extra fortune.";
            case IRON_HAND -> "Survive one bust per node.";
        };
    }

    private static String getDebuffName(DebuffType debuff) {
        return switch (debuff) {
            case CRACKED_HAND -> "Cracked Hand";
            case BLIND_DRAW -> "Blind Draw";
            case DEALERS_MARK -> "Dealer's Mark";
            case HEAVY_CARDS -> "Heavy Cards";
            case SHAKY_FINGERS -> "Shaky Fingers";
        };
    }

    private static String getDebuffDescription(DebuffType debuff) {
        return switch (debuff) {
            case CRACKED_HAND -> "Auto-stand at 19 instead of 21.";
            case BLIND_DRAW -> "Your first card is hidden from you.";
            case DEALERS_MARK -> "Dealer hits more aggressively.";
            case HEAVY_CARDS -> "Face cards count as 11 instead of 10.";
            case SHAKY_FINGERS -> "Cannot stand below 15.";
        };
    }

    private static void waitForContinue() {
        Scanner scanner = new Scanner(System.in);
        System.out.println(MenuManagement.BOLD + "\n    Press ENTER to continue..." + MenuManagement.RESET);
        scanner.nextLine();
    }
}
