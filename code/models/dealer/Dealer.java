package code.models.dealer;

import code.models.Hand;
import code.models.enums.DealerType;

public class Dealer {
    private String name;
    private Hand hand;
    private DealerType type;

    public Dealer(String name, DealerType type) {
        this.name = name;
        this.type = type;
        this.hand = new Hand();
    }

    public void resetHand() {
        hand.clear();
    }

    public Hand getHand() {
        return hand;
    }

    public String getName() {
        return name;
    }

    public DealerType getType() {
        return type;
    }

    public int getHitThreshold() {
        return 17;
    }

    public double getBlackjackBonus() {
        return 0.0;
    }

    public boolean shouldCheat() {
        return false;
    }

    public String getTitle() {
        return "Dealer";
    }
}
