package view;
import controller.MultiPlayerMenuController;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import models.game.GameType;

import java.io.FileNotFoundException;

public class FriendGameMenu extends GameModeChooseMenu {
    private static final String BACKGROUND_URL = "Client/resources/menu/background/friend_game_background.jpg";
    private static FriendGameMenu menu;

    private FriendGameMenu() throws FileNotFoundException {
        super(BACKGROUND_URL);
    }

    public static FriendGameMenu getInstance() {
        if (menu == null) {
            try {
                menu = new FriendGameMenu();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return menu;
    }

    @Override
    void startKillHero() {
        DialogWrapper dialog = new DialogWrapper(true, false);
        dialog.makeButton(GameType.KILL_HERO);
        dialog.show();
    }

}
