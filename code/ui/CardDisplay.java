package code.ui;

import java.util.List;

public class CardDisplay {
    private static final int CARD_WIDTH = 19;
    private static final int CARD_HEIGHT = 13;

    public static void displayCards(List<String> deck, int numCards, boolean isDealerCards, int showCardNumber) {
        printTopBorder(CARD_WIDTH, numCards, isDealerCards);
        System.out.println();
        for (int i = 0; i < CARD_HEIGHT - 2; i++) {
            printCardLine(deck.subList(0, numCards), i, numCards, isDealerCards, showCardNumber);
        }
        printBottomBorder(CARD_WIDTH, numCards, isDealerCards);
        System.out.println();
    }

    public static void displaySingleCard(String card) {
        List<String> singleList = List.of(card);
        printTopBorder(CARD_WIDTH, 1, false);
        System.out.println();
        for (int i = 0; i < CARD_HEIGHT - 2; i++) {
            printCardLine(singleList, i, 1, false, -1);
        }
        printBottomBorder(CARD_WIDTH, 1, false);
        System.out.println();
    }

    public static void displayFaceDownCard() {
        System.out.println("┌▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄┐");
        for (int i = 0; i < CARD_HEIGHT - 2; i++) {
            System.out.println("│█████████████████│");
        }
        System.out.println("└▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀┘");
    }

    private static void printCardLine(List<String> cards, int row, int numCards, boolean isDealerCards, int showCardNumber) {
        for (int i = 0; i < numCards; i++) {
            String card = cards.get(i);

            if (isDealerCards && i == 1) {
                if (row == 5 && i == numCards - 1) {
                    System.out.printf("│█████████████████│ Total value: [ %s ]", showCardNumber);
                } else {
                    System.out.print("│█████████████████│ ");
                }
            } else {
                String rank;
                String suit;

                if (card.equals("??")) {
                    rank = "?";
                    suit = "?";
                } else if (card.startsWith("§")) {
                    String clean = card.substring(1);
                    if (clean.length() > 1) {
                        rank = clean.substring(0, clean.length() - 1);
                        suit = clean.substring(clean.length() - 1);
                    } else {
                        rank = clean;
                        suit = " ";
                    }
                } else if (card.startsWith("★")) {
                    rank = "★";
                    suit = "✦";
                } else {
                    if (card.length() > 1) {
                        rank = card.substring(0, card.length() - 1);
                        suit = card.substring(card.length() - 1);
                    } else {
                        rank = card;
                        suit = " ";
                    }
                }

                System.out.print("│");

                boolean isCorrupted = card.startsWith("§");
                boolean isWild = card.startsWith("★");
                String prefix = isCorrupted ? "\u001B[31m" : (isWild ? "\u001B[33m" : "");
                String suffix = (isCorrupted || isWild) ? "\u001B[0m" : "";

                if (row == 0) {
                    if (rank.length() >= 2) {
                        System.out.printf(" %s%-3s%s             │", prefix, rank, suffix);
                    } else {
                        System.out.printf(" %s%-2s%s              │", prefix, rank, suffix);
                    }
                } else if (row == 5) {
                    if (i == numCards - 1 && showCardNumber >= 0) {
                        System.out.printf("        %s%-2s%s       │ Total value: [ %s ]", prefix, suit, suffix, showCardNumber);
                    } else {
                        System.out.printf("        %s%-2s%s       │", prefix, suit, suffix);
                    }
                } else if (row == 10) {
                    if (rank.length() >= 2) {
                        System.out.printf("              %s%-3s%s│", prefix, rank, suffix);
                    } else {
                        System.out.printf("               %s%-2s%s│", prefix, rank, suffix);
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
