package code.models.systems;

public class LivesSystem {
    private int currentLives;
    private int maxLives;

    public LivesSystem(int maxLives) {
        this.maxLives = maxLives;
        this.currentLives = maxLives;
    }

    public void loseLife() {
        if (currentLives > 0) {
            currentLives--;
        }
    }

    public void gainLife() {
        if (currentLives < maxLives) {
            currentLives++;
        }
    }

    public void reset() {
        currentLives = maxLives;
    }

    public boolean isAlive() {
        return currentLives > 0;
    }

    public int getCurrentLives() {
        return currentLives;
    }

    public int getMaxLives() {
        return maxLives;
    }

    public void setMaxLives(int maxLives) {
        this.maxLives = maxLives;
        if (currentLives > maxLives) {
            currentLives = maxLives;
        }
    }
}
