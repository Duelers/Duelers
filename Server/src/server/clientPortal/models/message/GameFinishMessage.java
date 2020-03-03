package server.clientPortal.models.message;

class GameFinishMessage {
    private boolean youWon;

    GameFinishMessage(boolean youWon) {
        this.youWon = youWon;
    }
}
