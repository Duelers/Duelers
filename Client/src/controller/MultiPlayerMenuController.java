package controller;

import Config.Config.Config;
import javafx.application.Platform;
import models.exceptions.InputException;
import models.game.GameType;
import models.message.Message;

public class MultiPlayerMenuController {
    private static final MultiPlayerMenuController ourInstance = new MultiPlayerMenuController();
    private static final String SERVER_NAME = Config.getInstance().getProperty("SERVER_NAME");

    private MultiPlayerMenuController() {
    }

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
