package code.models.dealer;

import code.models.enums.DealerType;
import java.util.Random;

public class BossDealer extends Dealer {
    private Random random;

    public BossDealer(String name) {
        super(name, DealerType.BOSS);
        this.random = new Random();
    }

    @Override
    public int getHitThreshold() {
        return 18;
    }

    @Override
    public double getBlackjackBonus() {
        return 0.2;
    }

    @Override
    public boolean shouldCheat() {
        return random.nextDouble() < 0.35;
    }

    @Override
    public String getTitle() {
        return "⚔ THE HOUSE ⚔";
    }
}
