package org.projectcardboard.client.view;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import org.projectcardboard.client.models.gui.PlayButtonItem;

import java.io.FileNotFoundException;

public class SinglePlayerMenu extends PlayMenu {
  private static final String BACKGROUND_URL = "/menu/background/single_player_background.jpg";
  private static final EventHandler<? super MouseEvent> BACK_EVENT =
      event -> PlayMenu.getInstance().show();
  private static final PlayButtonItem[] items =
      {new PlayButtonItem("/menu/playButtons/custom_game.jpg", "CUSTOM GAME",
          "Play with one of your decks controlled by AI",
          event -> CustomGameMenu.getInstance().show())};
  private static SinglePlayerMenu menu;

  private SinglePlayerMenu() throws FileNotFoundException {
    super(items, BACKGROUND_URL, BACK_EVENT);
  }

  public static SinglePlayerMenu getInstance() {
    if (menu == null) {
      try {
        menu = new SinglePlayerMenu();
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      }
    }
    return menu;
  }
}
