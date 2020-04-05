package org.projectcardboard.client.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shared.models.game.GameType;

import java.io.FileNotFoundException;

public class FriendGameMenu extends GameModeChooseMenu {
  private static final String BACKGROUND_URL = "/menu/background/friend_game_background.jpg";
  private static FriendGameMenu menu;

  private static Logger logger = LoggerFactory.getLogger(FriendGameMenu.class);

  private FriendGameMenu() throws FileNotFoundException {
    super(BACKGROUND_URL);
  }

  public static FriendGameMenu getInstance() {
    if (menu == null) {
      try {
        menu = new FriendGameMenu();
      } catch (FileNotFoundException e) {
        logger.warn("Error setting FriendGameMenu");
        logger.debug(e.getMessage());
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
