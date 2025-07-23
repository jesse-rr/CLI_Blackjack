package code;

import java.util.List;

public class CardManagement {
    private static final int CARD_WIDTH = 19;
    private static final int CARD_HEIGHT = 13;

    public static List<String> printCards(List<String> deck, int numCards, boolean isDealerCards, int showCardNumber) {
        printTopBorder(CARD_WIDTH, numCards, isDealerCards);
        System.out.println();
        for (int i = 0; i < CARD_HEIGHT - 2; i++) {
            printCardLine(deck.subList(0, numCards), i, numCards, isDealerCards, showCardNumber);
        }
        printBottomBorder(CARD_WIDTH, numCards, isDealerCards);
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
}
