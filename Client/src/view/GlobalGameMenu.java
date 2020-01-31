package view;


import controller.MultiPlayerMenuController;
import models.game.GameType;

import java.io.FileNotFoundException;

public class GlobalGameMenu extends GameModeChooseMenu {
    private static final String BACKGROUND_URL = "Client/resources/menu/background/global_game_background.jpg";
    private static GlobalGameMenu menu;

    private GlobalGameMenu() throws FileNotFoundException {
        super(BACKGROUND_URL);
    }

    public static GlobalGameMenu getInstance() {
        if (menu == null) {
            try {
                menu = new GlobalGameMenu();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return menu;
    }

    @Override
    void startKillHero() {
        MultiPlayerMenuController.getInstance().startGame(GameType.KILL_HERO, 0, null);
        new WaitingMenu(this).show();
    }

    @Override
    void startSingleFlag() {
        MultiPlayerMenuController.getInstance().startGame(GameType.A_FLAG, 1, null);
        new WaitingMenu(this).show();
    }

    @Override
    void startMultiFlag() {
        DialogWrapper dialog = new DialogWrapper(false, true);
        dialog.makeButton(GameType.SOME_FLAG, 0);
        dialog.show();
    }
}
