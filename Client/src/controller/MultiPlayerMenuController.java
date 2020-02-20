package controller;

import javafx.application.Platform;
import models.Constants;
import models.exceptions.InputException;
import localGameCenter.models.game.GameType;
import message.Message;

public class MultiPlayerMenuController {
    private static MultiPlayerMenuController ourInstance = new MultiPlayerMenuController();

    private MultiPlayerMenuController() {
    }

    public static MultiPlayerMenuController getInstance() {
        return ourInstance;
    }

    public void startGame(GameType gameType, String opponent) {
        try {
            if (opponent != null && opponent.length() < 2)
                throw new InputException("invalid opponent");
			if (gameType == GameType.LOCAL_HOTSEAT) { // TODO WIP
				// TODO: local
				// copy game engine
				// start game in gamecenter in a new thread
				// add a global var to lock messages in client
				// copy the big-ass switch case of the client (without the messages types) and call it here
			} else {
            Client.getInstance().addToSendingMessagesAndSend(
                    Message.makeMultiPlayerGameReQuestMessage(Constants.SERVER_NAME, gameType, opponent));
        }
    }  catch (InputException e) {
            Platform.runLater(() -> Client.getInstance().getCurrentShow().showError(e.getMessage()));
	}
}
}