package shared.models.game;

import shared.models.game.map.BaseGameMap;

public class BaseGame<PlayerType extends BasePlayer, GameMapType extends BaseGameMap> {
  protected final PlayerType playerOne;
  protected final PlayerType playerTwo;
  protected final GameMapType gameMap;
  protected int turnNumber;
  protected final GameType gameType;

  public BaseGame(PlayerType playerOne, PlayerType playerTwo, GameMapType gameMap, int turnNumber,
      GameType gameType) {
    this.playerOne = playerOne;
    this.playerTwo = playerTwo;
    this.gameMap = gameMap;
    this.turnNumber = turnNumber;
    this.gameType = gameType;
  }

  public PlayerType getPlayerOne() {
    return playerOne;
  }

  public PlayerType getPlayerTwo() {
    return playerTwo;
  }

  public GameMapType getGameMap() {
    return gameMap;
  }

  public int getTurnNumber() {
    return turnNumber;
  }

  public GameType getGameType() {
    return gameType;
  }

  public PlayerType getCurrentTurnPlayer() {
    return getPlayer(turnNumber % 2);
  }

  public PlayerType getOtherTurnPlayer() {
    return getPlayer(turnNumber % 2 + 1);
  }

  protected PlayerType getPlayer(int number) {
    if (number == 1) {
      return playerOne;
    } else {
      return playerTwo;
    }
  }

}
