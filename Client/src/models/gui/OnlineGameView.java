package models.gui;

import controller.OnlineGamesListController;
import javafx.scene.control.Button;
import models.game.GameType;
import server.clientPortal.models.message.OnlineGame;
import view.MainMenu;

public class OnlineGameView {
    private final String player1;
    private final String player2;
    private final GameType gameType;
    private final Button showButton = new Button("SHOW");

    OnlineGameView(OnlineGame onlineGame) {
        player1 = onlineGame.getPlayer1();
        player2 = onlineGame.getPlayer2();
        gameType = onlineGame.getGameType();

        showButton.setOnMouseClicked(event -> {
            OnlineGamesListController.getInstance().requestShowGame(onlineGame);
            MainMenu.getInstance().closeOnlineGamesList();
        });
    }

    public String getPlayer1() {
        return player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public GameType getGameType() {
        return gameType;
    }

    public Button getShowButton() {
        return showButton;
    }
}
