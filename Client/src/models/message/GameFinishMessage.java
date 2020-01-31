package models.message;

public class GameFinishMessage {
    private boolean youWon;
    private int reward;

    public int getReward() {
        return reward;
    }

    public boolean amIWinner() {
        return youWon;
    }
}
