package controller;

public class GameResultController {
    private static GameResultController gameResultController;
    private boolean amIWinner;
    private int reward;

    public static GameResultController getInstance() {
        if (gameResultController == null) {
            gameResultController = new GameResultController();
        }
        return gameResultController;
    }

    void setWinnerInfo(boolean amIWinner, int reward) {
        this.amIWinner = amIWinner;
        this.reward = reward;
    }

    public boolean amInWinner() {
        return amIWinner;
    }

    public int getReward() {
        return reward;
    }
}
