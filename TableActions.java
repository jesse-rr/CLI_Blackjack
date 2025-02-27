import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TableActions {

    public List<String> createDeck() {
        List<String> suits = Arrays.asList("♥", "♦", "♣", "♠");
        List<String> cardNumbers = Arrays.asList("2", "3", "4", "5", "6", "7", "8", "9", "10", "A", "J", "Q", "K");
        List<String> deck = new ArrayList<>();

        for (String num : cardNumbers) {
            for (String suit : suits) {
                deck.add(num + suit);
            }
        }

        Collections.shuffle(deck);
        return deck;
    }

    public int sumCards(List<String> cards) {
        int sum = 0;
        if (cards.size() == 2) {
            if (isTwoAces(cards)) {
                return 12;
            }
        }
        for (String card : cards) {
            String cardValue = card.replaceAll("[^0-9]", "");
            if (cardValue.isEmpty()) {
                if (card.charAt(0) == 'A') {
                    sum += 11;
                } else {
                    sum += 10;
                }
            } else {
                sum += Integer.parseInt(cardValue);
            }
        }
        return sum;
    }

    public boolean isTwoAces(List<String> cards) {
        return cards.get(0).charAt(0) == 'A' && cards.get(1).charAt(0) == 'A';
    }


    public boolean isBlackjack(List<String> cards) {
        return sumCards(cards) == 21 && cards.size() == 2;
    }

    public boolean canInsurance(String card) {
        return card.contains("A");
    }

//    public boolean canSplit(List<String> cards, int playerCardNumber) {
//        if (cards.get(0).substring(0,1).equals(cards.get(1).substring(0,1)) && playerCardNumber == 2) {
//            return true;
//        } else {
//            System.out.println("You can only slip on cards with equal ranks like: AA, 22, QQ");
//            return false;
//        }
//    }
//    public List<String> setExtraHands(List<String> deck, int i) {
//        return deck.subList(i+1, i+9);
//    } // TODO maybe


    public boolean canDoubleDown(List<String> cards, int playerCardNumber) {
        if (sumCards(cards.subList(0,playerCardNumber)) <= 11 && playerCardNumber == 2) {
            return true;
        } else {
            return false;
        }
    }

    public List<String> setDealerCards(List<String> deck) {
        return deck.subList(0,9); // 2 + 2 + 2 + 2 + 3 + 3 + 3 + 3 + 4 (BUST) -> 9 max
    }

    public List<String>  setPlayerCards(List<String> deck) {
        return deck.subList(10, 52);
    }

}
