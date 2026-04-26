package code.models;

import java.util.ArrayList;
import java.util.List;

public class Hand {
    private List<Card> cards;
    private int wildBonusTotal;

    public Hand() {
        this.cards = new ArrayList<>();
        this.wildBonusTotal = 0;
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public void addWildValue(int value) {
        wildBonusTotal += value;
    }

    public int getValue() {
        int value = wildBonusTotal;
        int aces = 0;

        for (Card card : cards) {
            if (card.isCorrupted()) continue;
            if (card.isWild()) continue;
            if (card.getRank().equals("A")) {
                value += 11;
                aces++;
            } else {
                value += card.getValue();
            }
        }

        while (value > 21 && aces > 0) {
            value -= 10;
            aces--;
        }

        return value;
    }

    public int getValueWithHeavyCards() {
        int value = wildBonusTotal;
        int aces = 0;

        for (Card card : cards) {
            if (card.isCorrupted()) continue;
            if (card.isWild()) continue;
            String rank = card.getRank();
            if (rank.equals("A")) {
                value += 11;
                aces++;
            } else if (rank.equals("K") || rank.equals("Q") || rank.equals("J")) {
                value += 11;
            } else {
                value += card.getValue();
            }
        }

        while (value > 21 && aces > 0) {
            value -= 10;
            aces--;
        }

        return value;
    }

    public boolean isBlackjack() {
        return cards.size() == 2 && getValue() == 21;
    }

    public List<Card> getCards() {
        return cards;
    }

    public int getSize() {
        return cards.size();
    }

    public void clear() {
        cards.clear();
        wildBonusTotal = 0;
    }

    public boolean hasWildCards() {
        return cards.stream().anyMatch(Card::isWild);
    }

    public int getCorruptedCount() {
        return (int) cards.stream().filter(Card::isCorrupted).count();
    }
}
