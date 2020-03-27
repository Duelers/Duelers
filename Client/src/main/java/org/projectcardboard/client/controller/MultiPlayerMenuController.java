package org.projectcardboard.client.controller;

import Config.Config;
import javafx.application.Platform;
import org.projectcardboard.client.models.exceptions.InputException;
import shared.models.game.GameType;
import org.projectcardboard.client.models.message.Message;

public class MultiPlayerMenuController {
  private static final MultiPlayerMenuController ourInstance = new MultiPlayerMenuController();
  private static final String SERVER_NAME = Config.getInstance().getProperty("SERVER_NAME");

  private MultiPlayerMenuController() {}

  public static MultiPlayerMenuController getInstance() {
    return ourInstance;
  }

  public void startGame(GameType gameType, String opponent) {
    try {
      if (opponent != null && opponent.length() < 2)
        throw new InputException("invalid opponent");
      Client.getInstance().addToSendingMessagesAndSend(
          Message.makeMultiPlayerGameReQuestMessage(SERVER_NAME, gameType, opponent));
    } catch (InputException e) {
      Platform.runLater(() -> Client.getInstance().getCurrentShow().showError(e.getMessage()));
    }
  }
}
