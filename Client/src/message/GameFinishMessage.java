package message;

public class GameFinishMessage {
    private boolean youWon;
    private int reward;

    GameFinishMessage(boolean youWon, int reward) {
        this.youWon = youWon;
        this.reward = reward;
    }

    public int getReward() {
        return reward;
    }

    public boolean amIWinner() {
        return youWon;
    }
}
