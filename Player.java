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
    public String getName() {
        return name;
    }
    public double getBalance() {
        return balance;
    }
    public double getWinRate() {
        return winRate;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setBalance(double balance) {
        this.balance = balance;
    }
    public void setWinRate(double winRate) {
        this.winRate = winRate;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
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
