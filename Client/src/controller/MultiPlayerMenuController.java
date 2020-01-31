package controller;

import javafx.application.Platform;
import models.Constants;
import models.exceptions.InputException;
import models.game.GameType;
import models.message.Message;

public class MultiPlayerMenuController {
    private static MultiPlayerMenuController ourInstance = new MultiPlayerMenuController();

    private MultiPlayerMenuController() {
    }

    public static MultiPlayerMenuController getInstance() {
        return ourInstance;
    }

    public void startGame(GameType gameType, int numberOfFlags, String opponent) {
        try {
            if (opponent != null && opponent.length() < 2)
                throw new InputException("invalid opponent");
            Client.getInstance().addToSendingMessagesAndSend(
                    Message.makeMultiPlayerGameReQuestMessage(Constants.SERVER_NAME, gameType,
                            numberOfFlags, opponent));
        } catch (InputException e) {
            Platform.runLater(() -> Client.getInstance().getCurrentShow().showError(e.getMessage()));
        }
    }
}
