package code.models;

public class Card {
    private String rank;
    private String suit;
    private boolean corrupted;
    private boolean wild;

    public Card(String rank, String suit) {
        this.rank = rank;
        this.suit = suit;
        this.corrupted = false;
        this.wild = false;
    }

    public Card(String rank, String suit, boolean corrupted) {
        this.rank = rank;
        this.suit = suit;
        this.corrupted = corrupted;
        this.wild = false;
    }

    public static Card createWild() {
        Card c = new Card("★", "✦");
        c.wild = true;
        return c;
    }

    public String getRank() {
        return rank;
    }

    public String getSuit() {
        return suit;
    }

    public boolean isCorrupted() {
        return corrupted;
    }

    public boolean isWild() {
        return wild;
    }

    public int getValue() {
        if (wild) return 0;
        if (rank.equals("A")) return 11;
        if (rank.equals("K") || rank.equals("Q") || rank.equals("J")) return 10;
        return Integer.parseInt(rank);
    }

    public String display() {
        if (wild) return "★✦";
        if (corrupted) return "§" + rank + suit;
        return rank + suit;
    }
}
