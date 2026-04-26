package code.ui;

import code.models.systems.GameMap;
import code.models.systems.MapNode;
import code.models.enums.MapNodeType;
import java.util.List;

public class MapDisplay {
    private static final String RESET = "\u001B[0m";
    private static final String BOLD = "\u001B[1m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String MAGENTA = "\u001B[35m";
    private static final String CYAN = "\u001B[36m";
    private static final String WHITE = "\u001B[37m";
    private static final String DIM = "\u001B[2m";
    private static final String STRIKETHROUGH = "\u001B[9m";

    public static void displayMap(GameMap gameMap, int currentRound) {
        System.out.println();
        System.out.println(BOLD + CYAN + "    ╔═══════════════════════════════════════════════════════╗" + RESET);
        System.out.println(BOLD + CYAN + "    ║                    G A M E   M A P                    ║" + RESET);
        System.out.println(BOLD + CYAN + "    ╚═══════════════════════════════════════════════════════╝" + RESET);
        System.out.println();

        for (int round = 1; round <= gameMap.getTotalRounds(); round++) {
            List<MapNode> nodes = gameMap.getNodesForRound(round);
            boolean isCurrentRound = (round == currentRound);
            boolean isPastRound = (round < currentRound);

            if (isPastRound) {
                displayConqueredRound(round, nodes);
            } else if (isCurrentRound) {
                displayCurrentRound(round, nodes);
            } else {
                displayLockedRound(round, nodes);
            }

            if (round < gameMap.getTotalRounds()) {
                if (isPastRound) {
                    System.out.println(DIM + "                      │" + RESET);
                } else if (isCurrentRound || round + 1 == currentRound) {
                    System.out.println(GREEN + "                      │" + RESET);
                } else {
                    System.out.println(DIM + WHITE + "                      │" + RESET);
                }
            }
        }

        if (currentRound <= gameMap.getTotalRounds()) {
            System.out.println(DIM + WHITE + "                      │" + RESET);
        } else {
            System.out.println(GREEN + "                      │" + RESET);
        }

        System.out.println("       └──────────────┼──────────────┘");
        System.out.println("                      ▼");

        boolean bossActive = currentRound > gameMap.getTotalRounds();
        String bossColor = bossActive ? RED + BOLD : DIM + WHITE;
        System.out.println(bossColor + "            ╔═════════════════╗" + RESET);
        System.out.println(bossColor + "            ║    THE HOUSE    ║" + RESET);
        System.out.println(bossColor + "            ║    FINAL BOSS   ║" + RESET);
        System.out.println(bossColor + "            ╚═════════════════╝" + RESET);

        System.out.println();
        displayLegend();
        System.out.println();
    }

    private static void displayConqueredRound(int round, List<MapNode> nodes) {
        MapNode visitedNode = null;
        for (MapNode n : nodes) {
            if (n.isVisited()) { visitedNode = n; break; }
        }

        System.out.print(DIM + "    Round " + round + " ");
        if (visitedNode != null) {
            System.out.println(CYAN + "✓ " + getNodeSymbol(visitedNode.getType()) + " " + visitedNode.getNodeName() + " cleared" + RESET);
        } else {
            System.out.println("✓ cleared" + RESET);
        }
    }

    private static void displayCurrentRound(int round, List<MapNode> nodes) {
        System.out.println(GREEN + BOLD + "    Round " + round + " ◄ YOU ARE HERE" + RESET);
        System.out.print("    ");
        for (int i = 0; i < nodes.size(); i++) {
            System.out.print(GREEN + BOLD + "┌─────────┐" + RESET + "  ");
        }
        System.out.println();

        System.out.print("    ");
        for (MapNode node : nodes) {
            String symbol = getNodeSymbol(node.getType());
            int pad = (9 - symbol.length()) / 2;
            String left = " ".repeat(Math.max(0, pad));
            String right = " ".repeat(Math.max(0, 9 - symbol.length() - pad));
            System.out.print(GREEN + BOLD + "│" + left + symbol + right + "│" + RESET + "  ");
        }
        System.out.println();

        System.out.print("    ");
        for (MapNode node : nodes) {
            String label = node.getNodeName();
            if (label.length() > 9) label = label.substring(0, 9);
            int pad = (9 - label.length()) / 2;
            String left = " ".repeat(Math.max(0, pad));
            String right = " ".repeat(Math.max(0, 9 - label.length() - pad));
            System.out.print(GREEN + BOLD + "│" + left + label + right + "│" + RESET + "  ");
        }
        System.out.println();

        System.out.print("    ");
        for (int i = 0; i < nodes.size(); i++) {
            System.out.print(GREEN + BOLD + "└─────────┘" + RESET + "  ");
        }
        System.out.println();
    }

    private static void displayLockedRound(int round, List<MapNode> nodes) {
        System.out.println(DIM + WHITE + "    Round " + round + " 🔒" + RESET);
        System.out.print("    ");
        for (int i = 0; i < nodes.size(); i++) {
            System.out.print(DIM + WHITE + "┌─────────┐" + RESET + "  ");
        }
        System.out.println();

        System.out.print("    ");
        for (MapNode node : nodes) {
            System.out.print(DIM + WHITE + "│   ???   │" + RESET + "  ");
        }
        System.out.println();

        System.out.print("    ");
        for (int i = 0; i < nodes.size(); i++) {
            System.out.print(DIM + WHITE + "│  Locked │" + RESET + "  ");
        }
        System.out.println();

        System.out.print("    ");
        for (int i = 0; i < nodes.size(); i++) {
            System.out.print(DIM + WHITE + "└─────────┘" + RESET + "  ");
        }
        System.out.println();
    }

    private static String getNodeSymbol(MapNodeType type) {
        return switch (type) {
            case DEALER -> "⚜";
            case CASHOUT -> "⛃";
            case HIDDEN -> "???";
            case BOSS -> "⚔";
        };
    }

    private static void displayLegend() {
        System.out.println(BOLD + "    LEGEND:" + RESET);
        System.out.println("    " + GREEN + BOLD + "┌───┐" + RESET + " Current\t" + DIM + WHITE + "┌───┐" + RESET + " Locked");
        System.out.println("    ⚜ Dealer\t??? Unknown\t⛃ Cashout\t⚔ Boss");
    }
}