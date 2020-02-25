package controller;

import models.message.DataName;
import models.message.Message;
import models.message.OnlineGame;
import Config.Config;

public class OnlineGamesListController {
    private static OnlineGamesListController controller;
    private OnlineGame[] onlineGames;
    private static final String SERVER_NAME = Config.getInstance().getProperty("SERVER_NAME");

    private OnlineGamesListController() {
    }

    public static OnlineGamesListController getInstance() {
        if (controller == null) {
            controller = new OnlineGamesListController();
        }
        return controller;
    }


    public void requestOnlineGamesList() {
        Client.getInstance().addToSendingMessagesAndSend(Message.makeGetDataMessage(SERVER_NAME, DataName.ONLINE_GAMES_LIST));
    }

    public void requestShowGame(OnlineGame onlineGame) {
        Client.getInstance().addToSendingMessagesAndSend(
                Message.makeRequestOnlineGameShowMessage(SERVER_NAME, onlineGame)
        );
    }

    synchronized void setOnlineGames(OnlineGame[] onlineGames) {
        this.onlineGames = onlineGames;
        this.notifyAll();
    }

    public OnlineGame[] getOnlineGames() {
        return onlineGames;
    }
}
