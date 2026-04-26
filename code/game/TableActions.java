package code.game;

import code.models.Card;
import code.models.Deck;
import code.models.Hand;
import code.models.enums.DeckTheme;
import java.util.Random;

public class TableActions {
    private Deck deck;
    private Random random;
    private static final int BLACKJACK_VALUE = 21;

    public TableActions() {
        this.random = new Random();
    }

    public void initializeDeck(DeckTheme theme, int numberOfDecks) {
        this.deck = new Deck(theme, numberOfDecks);
    }

    public Card drawCard() {
        return deck.drawCard();
    }

    public Card drawCheatCard(Hand dealerHand) {
        int currentValue = dealerHand.getValue();
        int target = 17 + random.nextInt(5);
        int needed = target - currentValue;
        if (needed < 2) needed = 2;
        if (needed > 11) needed = 10;

        String rank;
        if (needed == 11) rank = "A";
        else if (needed == 10) {
            String[] faces = {"10", "J", "Q", "K"};
            rank = faces[random.nextInt(faces.length)];
        } else {
            rank = String.valueOf(needed);
        }

        String[] suits = {"♥", "♦", "♣", "♠"};
        return new Card(rank, suits[random.nextInt(suits.length)]);
    }

    public Card drawAdvantagedCard(Hand dealerHand) {
        if (dealerHand.getSize() == 0) {
            if (random.nextDouble() < 0.15) {
                return new Card("A", "♠");
            }
        }
        if (dealerHand.getSize() == 1 && dealerHand.getCards().get(0).getRank().equals("A")) {
            String[] tens = {"10", "J", "Q", "K"};
            return new Card(tens[random.nextInt(tens.length)], "♠");
        }
        return drawCard();
    }

    public int sumCards(Hand hand) {
        return hand.getValue();
    }

    public boolean isBlackjack(Hand hand) {
        return hand.isBlackjack();
    }

    public boolean shouldDealerHit(int dealerTotal, int threshold) {
        return dealerTotal < threshold;
    }

    public boolean isBust(int total) {
        return total > BLACKJACK_VALUE;
    }

    public Deck getDeck() {
        return deck;
    }
}
