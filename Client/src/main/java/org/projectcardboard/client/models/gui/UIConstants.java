package org.projectcardboard.client.models.gui;

import java.io.FileNotFoundException;
import java.io.InputStream;

import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UIConstants {
  public static final Background DEFAULT_ROOT_BACKGROUND =
      new Background(new BackgroundFill(Color.rgb(44, 33, 129), CornerRadii.EMPTY, Insets.EMPTY));
  private static final double DEFAULT_SCENE_WIDTH = 3636;
  private static final double DEFAULT_SCENE_HEIGHT = 2045;
  public static final double SCALE =
      Math.min(Screen.getPrimary().getBounds().getWidth() / DEFAULT_SCENE_WIDTH,
          Screen.getPrimary().getBounds().getHeight() / DEFAULT_SCENE_HEIGHT);
  public static final double DEFAULT_SPACING = 10 * SCALE;
  public static final Font DEFAULT_FONT = Font.font("SansSerif", FontWeight.BOLD, 25 * SCALE);
  static final double DUELYST_LOGO_WIDTH = 500 * SCALE;
  static final double DUELYST_LOGO_HEIGHT = 108 * SCALE;

  public static final double SCENE_WIDTH = DEFAULT_SCENE_WIDTH * SCALE;
  public static final double SCENE_HEIGHT = DEFAULT_SCENE_HEIGHT * SCALE;
  public static Cursor DEFAULT_CURSOR;
  public static Cursor SELECT_CURSOR;

  private static Logger logger = LoggerFactory.getLogger(UIConstants.class);

  static {
    try {
      InputStream defaultCursorResource =
          UIConstants.class.getResourceAsStream("/cursors/default.png");
      if (defaultCursorResource == null) {
        throw new FileNotFoundException();
      }
      UIConstants.DEFAULT_CURSOR = new ImageCursor(new Image(defaultCursorResource));

      InputStream selectCursorResource =
          UIConstants.class.getResourceAsStream("/cursors/select.png");
      if (selectCursorResource == null) {
        throw new FileNotFoundException();
      }
      UIConstants.SELECT_CURSOR = new ImageCursor(new Image(selectCursorResource));
    } catch (FileNotFoundException e) {
      logger.warn("Could not find file");
      logger.trace(e.getMessage());
    }
  }
}
