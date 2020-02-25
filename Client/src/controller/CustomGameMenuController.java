package controller;

import Config.Config;
import models.game.GameType;
import models.message.Message;

public class CustomGameMenuController {
    private static CustomGameMenuController ourInstance;
    private static final String SERVER_NAME = Config.getInstance().getProperty("SERVER_NAME");

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
                Message.makeNewCustomGameMessage(SERVER_NAME, gameType, deckName));
    }
}
