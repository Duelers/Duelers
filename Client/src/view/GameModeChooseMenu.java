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
            new PlayButtonItem("Client/resources/menu/playButtons/single_flag.jpg", "SINGLE FLAG",
                    "You must keep the flag for 6 turns to win", event -> menu.startSingleFlag()),
            new PlayButtonItem("Client/resources/menu/playButtons/multi_flag.jpg", "MULTI FLAG",
                    "You must collect at least half the flags to win", event -> menu.startMultiFlag())
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

    abstract void startSingleFlag();

    abstract void startMultiFlag();

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

        void makeButton(GameType type, int numberOfFlags) {
            box.makeButton("START", buttonEvent -> {
                if (usernameField != null && "".equals(usernameField.getText())) return;
                MultiPlayerMenuController.getInstance().startGame(
                        type,
                        flagNumSpinner == null ? numberOfFlags : flagNumSpinner.getValue(),
                        usernameField == null ? null : usernameField.getText()
                );
                container.close();
                new WaitingMenu(menu).show();
            });
        }
    }
}
