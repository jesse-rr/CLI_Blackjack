package code.models;

public class Player {
    private String name;
    private String password;
    private double balance;
    private double winRate;
    private int wins;
    private int loses;

    public Player(String name, String password, double balance, double winRate, int wins, int loses) {
        this.name = name;
        this.password = password;
        this.balance = balance;
        this.winRate = winRate;
        this.wins = wins;
        this.loses = loses;
    }
    public void setBalance(double balance) {
        if (balance < 0) throw new IllegalArgumentException("Balance cannot be negative");
        this.balance = Math.round(balance * 100) / 100.0;
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
    }

    private void updateWinRate() {
        if (wins + loses > 0) {
            winRate = Math.round(100.0 * wins / (wins + loses));
        }
    }

    public boolean canAfford(double amount) {
        return balance >= amount;
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

    public double getBalance() {
        return balance;
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
}