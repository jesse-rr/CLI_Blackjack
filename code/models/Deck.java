package code.models;

import code.models.enums.DeckTheme;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Deck {
    private List<Card> cards;
    private DeckTheme theme;
    private int currentIndex = 0;

    private static final String[] SUITS = {"♥", "♦", "♣", "♠"};
    private static final String[] RANKS = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "A", "J", "Q", "K"};

    public Deck(DeckTheme theme, int numberOfDecks) {
        this.theme = theme;
        this.cards = createDeck(numberOfDecks);
    }

    private List<Card> createDeck(int numberOfDecks) {
        List<Card> deck = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < numberOfDecks; i++) {
            for (String rank : RANKS) {
                for (String suit : SUITS) {
                    deck.add(new Card(rank, suit));
                }
            }
        }

        if (theme == DeckTheme.CORRUPTED) {
            int totalCards = deck.size();
            int corruptedCount = totalCards / 3;
            List<Card> corruptedCards = new ArrayList<>();
            for (int i = 0; i < corruptedCount; i++) {
                String rank = RANKS[random.nextInt(RANKS.length)];
                String suit = SUITS[random.nextInt(SUITS.length)];
                corruptedCards.add(new Card(rank, suit, true));
            }
            deck.addAll(corruptedCards);
        }

        if (theme == DeckTheme.FOOLS) {
            int wildCount = deck.size() / 4;
            for (int i = 0; i < wildCount; i++) {
                deck.add(Card.createWild());
            }
        }

        Collections.shuffle(deck);
        return deck;
    }

    public Card drawCard() {
        if (currentIndex >= cards.size()) {
            reshuffle(1);
        }
        return cards.get(currentIndex++);
    }

    public void reshuffle(int numberOfDecks) {
        this.cards = createDeck(numberOfDecks);
        this.currentIndex = 0;
    }

    public DeckTheme getTheme() {
        return theme;
    }

    public int getRemainingCards() {
        return cards.size() - currentIndex;
    }
}
