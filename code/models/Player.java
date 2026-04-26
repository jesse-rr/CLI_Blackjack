package code.models;

import code.models.enums.BuffType;
import code.models.enums.DebuffType;
import code.models.enums.SkillType;
import code.models.systems.LivesSystem;
import code.models.systems.GameMap;
import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    private String password;
    private double gemstones;
    private double winRate;
    private int wins;
    private int loses;
    private LivesSystem livesSystem;
    private SkillType chosenSkill;
    private List<BuffType> activeBuffs;
    private List<DebuffType> activeDebuffs;
    private GameMap gameMap;
    private int skillUsesRemaining;

    public Player(String name, String password, double gemstones, double winRate, int wins, int loses) {
        this.name = name;
        this.password = password;
        this.gemstones = gemstones;
        this.winRate = winRate;
        this.wins = wins;
        this.loses = loses;
        this.livesSystem = new LivesSystem(3);
        this.activeBuffs = new ArrayList<>();
        this.activeDebuffs = new ArrayList<>();
        this.gameMap = new GameMap();
        this.skillUsesRemaining = 0;
    }

    public void setGemstones(double gemstones) {
        if (gemstones < 0) gemstones = 0;
        this.gemstones = Math.round(gemstones * 100) / 100.0;
    }

    public void setWinRate(double winRate) {
        this.winRate = Math.max(0, Math.min(100, winRate));
    }

    public void addWin() {
        wins++;
        updateWinRate();
    }

    public void addLoss() {
        loses++;
        updateWinRate();
        livesSystem.loseLife();
    }

    private void updateWinRate() {
        if (wins + loses > 0) {
            winRate = Math.round(100.0 * wins / (wins + loses));
        }
    }

    public void resetLives() {
        livesSystem.reset();
    }

    public void setChosenSkill(SkillType skill) {
        this.chosenSkill = skill;
        this.skillUsesRemaining = 3;
    }

    public SkillType getChosenSkill() {
        return chosenSkill;
    }

    public int getSkillUsesRemaining() {
        return skillUsesRemaining;
    }

    public boolean useSkill() {
        if (skillUsesRemaining > 0) {
            skillUsesRemaining--;
            return true;
        }
        return false;
    }

    public void refundSkill() {
        skillUsesRemaining++;
    }

    public void addBuff(BuffType buff) {
        if (!activeBuffs.contains(buff)) {
            activeBuffs.add(buff);
        }
    }

    public void removeBuff(BuffType buff) {
        activeBuffs.remove(buff);
    }

    public boolean hasBuff(BuffType buff) {
        return activeBuffs.contains(buff);
    }

    public List<BuffType> getActiveBuffs() {
        return new ArrayList<>(activeBuffs);
    }

    public void addDebuff(DebuffType debuff) {
        if (!activeDebuffs.contains(debuff)) {
            activeDebuffs.add(debuff);
        }
    }

    public void removeDebuff(DebuffType debuff) {
        activeDebuffs.remove(debuff);
    }

    public boolean hasDebuff(DebuffType debuff) {
        return activeDebuffs.contains(debuff);
    }

    public List<DebuffType> getActiveDebuffs() {
        return new ArrayList<>(activeDebuffs);
    }

    public void clearAllDebuffs() {
        activeDebuffs.clear();
    }

    public void clearAllBuffs() {
        activeBuffs.clear();
    }

    public void resetForNewGame() {
        livesSystem.reset();
        activeBuffs.clear();
        activeDebuffs.clear();
        gameMap = new GameMap();
        skillUsesRemaining = 0;
        chosenSkill = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getGemstones() {
        return gemstones;
    }

    public double getWinRate() {
        return winRate;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLoses() {
        return loses;
    }

    public void setLoses(int loses) {
        this.loses = loses;
    }

    public LivesSystem getLivesSystem() {
        return livesSystem;
    }

    public GameMap getGameMap() {
        return gameMap;
    }

    public String getLivesDisplay() {
        int current = livesSystem.getCurrentLives();
        int max = livesSystem.getMaxLives();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < current; i++) sb.append("♥");
        for (int i = current; i < max; i++) sb.append("♡");
        sb.append(" ").append(current).append("/").append(max);
        return sb.toString();
    }
}