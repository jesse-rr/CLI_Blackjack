package code.models.systems;

import code.models.enums.MapNodeType;
import code.models.enums.HiddenNodeOutcome;
import java.util.Random;

public class MapNode {
    private int nodeId;
    private MapNodeType type;
    private int roundNumber;
    private boolean visited;
    private String nodeName;
    private HiddenNodeOutcome hiddenOutcome;

    public MapNode(int nodeId, MapNodeType type, int roundNumber) {
        this.nodeId = nodeId;
        this.type = type;
        this.roundNumber = roundNumber;
        this.visited = false;
        this.nodeName = generateName(type);
        if (type == MapNodeType.HIDDEN) {
            this.hiddenOutcome = resolveHiddenOutcome();
        }
    }

    private String generateName(MapNodeType type) {
        return switch (type) {
            case DEALER -> "Dealer";
            case CASHOUT -> "Cashout";
            case HIDDEN -> "???";
            case BOSS -> "THE HOUSE";
        };
    }

    private HiddenNodeOutcome resolveHiddenOutcome() {
        Random random = new Random();
        double roll = random.nextDouble();
        if (roll < 0.25) return HiddenNodeOutcome.LOSE_LIFE;
        if (roll < 0.50) return HiddenNodeOutcome.DEALER;
        if (roll < 0.75) return HiddenNodeOutcome.CASHOUT;
        return HiddenNodeOutcome.DEBUFF;
    }

    public void markAsVisited() {
        visited = true;
    }

    public int getNodeId() {
        return nodeId;
    }

    public MapNodeType getType() {
        return type;
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public boolean isVisited() {
        return visited;
    }

    public String getNodeName() {
        return nodeName;
    }

    public HiddenNodeOutcome getHiddenOutcome() {
        return hiddenOutcome;
    }
}
