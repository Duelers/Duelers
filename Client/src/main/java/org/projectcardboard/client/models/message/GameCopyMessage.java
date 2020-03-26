package org.projectcardboard.client.models.message;

import org.projectcardboard.client.models.game.Game;

public class GameCopyMessage {
  private Game game;
  private int p1StartingDeckSize;
  private int p2StartingDeckSize;

  public Game getGame() {
    return game;
  }

  public int getP1StartingDeckSize() {
    return p1StartingDeckSize;
  }

  public int getP2StartingDeckSize() {
    return p2StartingDeckSize;
  }
}
