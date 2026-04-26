package code.models.systems;

import code.models.enums.MapNodeType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameMap {
    private List<List<MapNode>> rounds;
    private MapNode bossNode;
    private int currentRound;
    private static final int TOTAL_ROUNDS = 6;

    public GameMap() {
        this.rounds = new ArrayList<>();
        this.currentRound = 1;
        initializeMap();
    }

    private void initializeMap() {
        int nodeId = 0;

        List<Integer> cashoutRounds = new ArrayList<>();
        for (int i = 1; i <= TOTAL_ROUNDS; i++) {
            cashoutRounds.add(i);
        }
        Collections.shuffle(cashoutRounds);
        List<Integer> selectedCashouts = cashoutRounds.subList(0, 2);

        for (int round = 1; round <= TOTAL_ROUNDS; round++) {
            List<MapNodeType> types = new ArrayList<>();
            types.add(MapNodeType.DEALER);
            if (selectedCashouts.contains(round)) {
                types.add(MapNodeType.CASHOUT);
            } else {
                types.add(MapNodeType.HIDDEN);
            }
            types.add(MapNodeType.HIDDEN);
            Collections.shuffle(types);

            List<MapNode> roundNodes = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                roundNodes.add(new MapNode(nodeId++, types.get(i), round));
            }
            rounds.add(roundNodes);
        }

        bossNode = new MapNode(nodeId, MapNodeType.BOSS, TOTAL_ROUNDS + 1);
    }

    public List<MapNode> getNodesForRound(int round) {
        if (round >= 1 && round <= TOTAL_ROUNDS) {
            return rounds.get(round - 1);
        }
        return new ArrayList<>();
    }

    public MapNode getBossNode() {
        return bossNode;
    }

    public int getCurrentRound() {
        return currentRound;
    }

    public void advanceRound() {
        if (currentRound <= TOTAL_ROUNDS) {
            currentRound++;
        }
    }

    public boolean isBossRound() {
        return currentRound > TOTAL_ROUNDS;
    }

    public int getTotalRounds() {
        return TOTAL_ROUNDS;
    }

    public List<List<MapNode>> getAllRounds() {
        return rounds;
    }
}
