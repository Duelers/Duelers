package server.clientPortal.models.message;

class GameFinishMessage {
  private final boolean youWon;

  GameFinishMessage(boolean youWon) {
    this.youWon = youWon;
  }
}
