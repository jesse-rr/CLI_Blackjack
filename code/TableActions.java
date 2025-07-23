package code;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TableActions {
    private static final List<String> SUITS = Arrays.asList("♥", "♦", "♣", "♠");
    private static final List<String> RANKS = Arrays.asList("2", "3", "4", "5", "6", "7", "8", "9", "10", "A", "J", "Q", "K");
    private static final int BLACKJACK_VALUE = 21;
    private static final int DEALER_HIT_THRESHOLD = 17;
    private static final int DOUBLE_DOWN_MAX_VALUE = 11;
    private List<String> deck;
    private int currentCardIndex = 0;

    public void initializeDeck(int numberOfDecks) {
        this.deck = createDeck(numberOfDecks);
        this.currentCardIndex = 0;
    }

    public List<String> createDeck(int numberOfDecks) {
        List<String> deck = new ArrayList<>();
        for (int i = 0; i < numberOfDecks; i++) {
            for (String rank : RANKS) {
                for (String suit : SUITS) {
                    deck.add(rank + suit);
                }
            }
        }
        Collections.shuffle(deck);
        return deck;
    }

    public int sumCards(List<String> cards) {
        int sum = 0;
        int aces = 0;

        for (String card : cards) {
            String rank = card.substring(0, card.length() - 1);
            if (rank.equals("A")) {
                sum += 11;
                aces++;
            } else if (rank.equals("K") || rank.equals("Q") || rank.equals("J")) {
                sum += 10;
            } else {
                sum += Integer.parseInt(rank);
            }
        }

        // Handle soft aces
        while (sum > BLACKJACK_VALUE && aces > 0) {
            sum -= 10;
            aces--;
        }

        return sum;
    }

    public boolean isBlackjack(List<String> cards) {
        return cards.size() == 2 && sumCards(cards) == BLACKJACK_VALUE;
    }

    public boolean shouldDealerHit(int dealerTotal) {
        return dealerTotal < DEALER_HIT_THRESHOLD;
    }

    public String drawCard() {
        if (currentCardIndex >= deck.size()) {
            initializeDeck(1);
        }
        return deck.get(currentCardIndex++);
    }
}