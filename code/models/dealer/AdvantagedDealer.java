package code.models.dealer;

import code.models.enums.DealerType;

public class AdvantagedDealer extends Dealer {

    public AdvantagedDealer(String name) {
        super(name, DealerType.ADVANTAGED);
    }

    @Override
    public int getHitThreshold() {
        return 18;
    }

    @Override
    public double getBlackjackBonus() {
        return 0.15;
    }

    @Override
    public String getTitle() {
        return "Advantaged Dealer";
    }
}
