package view;

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
        dialog.makeButton(GameType.KILL_HERO, 0);
        dialog.show();
    }

    @Override
    void startSingleFlag() {
        DialogWrapper dialog = new DialogWrapper(true, false);
        dialog.makeButton(GameType.A_FLAG, 1);
        dialog.show();
    }

    @Override
    void startMultiFlag() {
        DialogWrapper dialog = new DialogWrapper(true, true);
        dialog.makeButton(GameType.SOME_FLAG, 0);
        dialog.show();
    }
}
