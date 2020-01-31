package view;

import controller.GraphicalUserInterface;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.paint.Color;
import models.Constants;
import models.gui.*;

import java.io.File;
import java.io.FileNotFoundException;

public class PlayMenu extends Show {
    private static final Background ROOT_BACKGROUND = new Background(
            new BackgroundFill(
                    Color.rgb(40, 43, 53), CornerRadii.EMPTY, Insets.EMPTY
            )
    );
    private static final String BACKGROUND_URL = "Client/resources/menu/background/play_background.jpg";
    private static final EventHandler<? super MouseEvent> BACK_EVENT = event -> new MainMenu().show();
    private static Media backgroundMusic = new Media(
            new File("Client/resources/music/play_menu.m4a").toURI().toString()
    );
    private static final PlayButtonItem[] items = {
            new PlayButtonItem("Client/resources/menu/playButtons/single_player.jpg", "SINGLE PLAYER",
                    "Story game and custom game, play with AI", event -> SinglePlayerMenu.getInstance().show()),
            new PlayButtonItem("Client/resources/menu/playButtons/friend_game.jpg", "PLAY WITH FRIENDS",
                    "Play with your friends and earn money", event -> FriendGameMenu.getInstance().show()),
            new PlayButtonItem("Client/resources/menu/playButtons/global_game.jpg", "GLOBAL GAME",
                    "Search for an opponent an play", event -> GlobalGameMenu.getInstance().show())
    };
    private static PlayMenu menu;

    PlayMenu(PlayButtonItem[] items, String backgroundUrl, EventHandler<? super MouseEvent> backEvent) throws FileNotFoundException {
        root.setBackground(ROOT_BACKGROUND);

        BorderPane background = BackgroundMaker.getPlayBackground(backgroundUrl);
        DefaultContainer container = new DefaultContainer(new HorizontalButtonsBox(items));
        BackButton backButton = new BackButton(backEvent);

        AnchorPane sceneContents = new AnchorPane(background, container, backButton);

        root.getChildren().addAll(sceneContents);
    }

    public static PlayMenu getInstance() {
        if (menu == null) {
            try {
                menu = new PlayMenu(items, BACKGROUND_URL, BACK_EVENT);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return menu;
    }

    private void showGlobalChatDialog(AnchorPane sceneContents) {
        sceneContents.setOnKeyPressed(event -> {
            if (event.getCode().equals(Constants.KEY_FOR_CHAT)) {
                GlobalChatDialog.getInstance().show();
            }
        });
    }

    @Override
    public void show() {
        super.show();
        GraphicalUserInterface.getInstance().setBackgroundMusic(backgroundMusic);
    }
}
