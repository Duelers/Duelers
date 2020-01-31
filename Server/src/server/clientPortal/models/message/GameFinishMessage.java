package server.clientPortal.models.message;

class GameFinishMessage {
    private boolean youWon;

    private int reward;


    GameFinishMessage(boolean youWon, int reward) {
        this.youWon = youWon;
        this.reward = reward;
    }
}
