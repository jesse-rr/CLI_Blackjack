package code.models.dealer;

import code.models.enums.DealerType;
import java.util.Random;

public class RogueDealer extends Dealer {
    private Random random;

    public RogueDealer(String name) {
        super(name, DealerType.ROGUE);
        this.random = new Random();
    }

    @Override
    public boolean shouldCheat() {
        return random.nextDouble() < 0.3;
    }

    @Override
    public String getTitle() {
        return "Rogue Dealer";
    }
}
