package controller;

import models.Constants;
import server.gameCenter.models.game.GameType;
import server.clientPortal.models.message.Message;

public class CustomGameMenuController {
    private static CustomGameMenuController ourInstance;

    private CustomGameMenuController() {
    }

    public static CustomGameMenuController getInstance() {
        if (ourInstance == null) {
            ourInstance = new CustomGameMenuController();
        }
        return ourInstance;
    }

    public void startGame(String deckName, GameType gameType) {
        Client.getInstance().addToSendingMessagesAndSend(
                Message.makeNewCustomGameMessage(Constants.SERVER_NAME, gameType, deckName));
    }
}
