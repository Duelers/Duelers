package server.clientPortal.models.message;

import shared.models.game.GameType;

public class NewGameFields {
  private final GameType gameType;
  private final int stage;
  private final String customDeckName;
  private final String opponentUsername;

  public NewGameFields(GameType gameType, int stage, String customDeckName,
      String opponentUsername) {
    this.gameType = gameType;
    this.stage = stage;
    this.customDeckName = customDeckName;
    this.opponentUsername = opponentUsername;
  }

  public GameType getGameType() {
    return gameType;
  }

  public int getStage() {
    return stage;
  }

  public String getCustomDeckName() {
    return customDeckName;
  }

  public String getOpponentUsername() {
    return opponentUsername;
  }
}
