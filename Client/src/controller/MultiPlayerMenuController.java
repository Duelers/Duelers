package controller;

import javafx.application.Platform;
import models.Constants;
import server.exceptions.InputException;
import server.gameCenter.models.game.GameType;
import server.clientPortal.models.message.Message;

public class MultiPlayerMenuController {
    private static final MultiPlayerMenuController ourInstance = new MultiPlayerMenuController();

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
                    Message.makeMultiPlayerGameReQuestMessage(Constants.SERVER_NAME, gameType, opponent));
        } catch (InputException e) {
            Platform.runLater(() -> Client.getInstance().getCurrentShow().showError(e.getMessage()));
        }
    }
}
