package view;

import controller.MultiPlayerMenuController;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import models.game.GameType;
import models.gui.*;

import java.io.FileNotFoundException;

public abstract class GameModeChooseMenu extends PlayMenu {
    private static final EventHandler<? super MouseEvent> BACK_EVENT = event -> PlayMenu.getInstance().show();
    private static GameModeChooseMenu menu;
    private static final PlayButtonItem[] items = {
            new PlayButtonItem("Client/resources/menu/playButtons/kill_hero.jpg", "KILL HERO",
                    "You must kill opponent's hero to win", event -> menu.startKillHero()),
    };

    GameModeChooseMenu(String backgroundUrl) throws FileNotFoundException {
        super(items, backgroundUrl, BACK_EVENT);
    }

    @Override
    public void show() {
        menu = this;
        super.show();
    }

    abstract void startKillHero();

    class DialogWrapper {
        private final DialogBox box;
        private final DialogContainer container;
        private NormalField usernameField;
        private FlagSpinner flagNumSpinner;

        DialogWrapper(boolean username, boolean spinner) {
            box = new DialogBox();
            container = new DialogContainer(root, box);
            if (username) {
                DialogText usernameText = new DialogText("Please enter opponent's username");
                usernameField = new NormalField("opponent username");
                box.getChildren().addAll(usernameText, usernameField);
            }
            if (spinner) {
                DialogText flagNumText = new DialogText("Please set number of flags in the game");
                flagNumSpinner = new FlagSpinner();
                box.getChildren().addAll(flagNumText, flagNumSpinner);
            }
        }

        void show() {
            container.show();
            box.makeClosable(container);
        }

        void makeButton(GameType type) {
            box.makeButton("START", buttonEvent -> {
                if (usernameField != null && "".equals(usernameField.getText())) return;
                MultiPlayerMenuController.getInstance().startGame(
                        type,
                        usernameField == null ? null : usernameField.getText()
                );
                container.close();
                new WaitingMenu(menu).show();
            });
        }
    }
}
