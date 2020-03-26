package server.clientPortal.models.message;

import server.gameCenter.models.game.Game;
import shared.models.game.GameType;

public class OnlineGame {
  private final String player1;
  private final String player2;
  private final GameType gameType;

  public OnlineGame(Game game) {
    player1 = game.getPlayerOne().getUserName();
    player2 = game.getPlayerTwo().getUserName();
    gameType = game.getGameType();
  }

  public String getPlayer1() {
    return player1;
  }

  public String getPlayer2() {
    return player2;
  }

  public GameType getGameType() {
    return gameType;
  }
}
