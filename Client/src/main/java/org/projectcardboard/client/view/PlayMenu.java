package org.projectcardboard.client.view;

import org.projectcardboard.client.controller.GraphicalUserInterface;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.paint.Color;
import org.projectcardboard.client.models.gui.*;

import java.io.FileNotFoundException;

public class PlayMenu extends Show {
  private static final Background ROOT_BACKGROUND =
      new Background(new BackgroundFill(Color.rgb(40, 43, 53), CornerRadii.EMPTY, Insets.EMPTY));
  private static final String BACKGROUND_URL = "/menu/background/play_background.jpg";
  private static final EventHandler<? super MouseEvent> BACK_EVENT = event -> new MainMenu().show();
  private static final Media backgroundMusic =
      new Media(PlayMenu.class.getResource("/music/play_menu.m4a").toString());
  private static final PlayButtonItem[] items = {
      new PlayButtonItem("/menu/playButtons/single_player.jpg", "SINGLE PLAYER",
          "Custom game, play with AI", event -> CustomGameMenu.getInstance().show()),
      new PlayButtonItem("/menu/playButtons/friend_game.jpg", "PLAY WITH FRIENDS",
          "Play with your friends", event -> FriendGameMenu.getInstance().show()),
      new PlayButtonItem("/menu/playButtons/global_game.jpg", "GLOBAL GAME",
          "Search for an opponent an play", event -> GlobalGameMenu.getInstance().show())};
  private static PlayMenu menu;

  PlayMenu(PlayButtonItem[] items, String backgroundUrl, EventHandler<? super MouseEvent> backEvent)
      throws FileNotFoundException {
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

  @Override
  public void show() {
    super.show();
    GraphicalUserInterface.getInstance().setBackgroundMusic(backgroundMusic);
  }
}
