package org.projectcardboard.client.view.battleview;

import Config.Config;
import javafx.geometry.Insets;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Constants {
  static final double SCREEN_WIDTH =
      Double.parseDouble(Config.getInstance().getProperty("CONFIG_SCREEN_WIDTH"));
  static final double SCREEN_HEIGHT =
      Double.parseDouble(Config.getInstance().getProperty("CONFIG_SCREEN_HEIGHT"));
  static final double SCALE = SCREEN_WIDTH / 1920;
  // MAP
  static final double MAP_X = SCREEN_WIDTH * 0.2, MAP_Y = SCREEN_HEIGHT * 0.3;
  static final double MAP_DOWNER_WIDTH = SCREEN_WIDTH * 0.56, MAP_UPPER_WIDTH = SCREEN_WIDTH * 0.42;
  static final double MAP_HEIGHT = (MAP_DOWNER_WIDTH + MAP_UPPER_WIDTH) * 5 / 16;
  static final double HAND_X = SCREEN_WIDTH * 0.06;
  static final double HAND_Y = SCREEN_HEIGHT * 0.8;
  static final double TROOP_SCALE = 1.7;
  static final double SPELL_SCALE = 1.2;
  static final double CELLS_DEFAULT_OPACITY = 0.4;
  static final double SPACE_BETWEEN_CELLS = 2;
  static final double MOVE_TIME_PER_CELL = 200;

  static final Font NAME_FONT = Font.font("SansSerif", FontWeight.BOLD, 30 * SCALE);
  static final Font AP_FONT = Font.font("SansSerif", FontWeight.LIGHT, 20 * SCALE);
  static final Font END_TURN_FONT = Font.font("SansSerif", FontWeight.BOLD, 20 * SCALE);
  static final Insets NAME_PADDING = new Insets(10 * SCALE);

  static final Color DEPLOY_TROOP = Color.rgb(225, 210, 25, 1);

  static final Color CAN_ATTACK = Color.rgb(255, 157, 0, .6);
  static final Color ATTACK_COLOR = Color.rgb(255, 157, 0);

  static final Color CAN_MOVE = Color.rgb(21, 255, 0, .9);
  static final Color MOVE_COLOR = Color.rgb(255, 255, 255, 0.6);
  static final Color SELECTED_COLOR = Color.rgb(255, 255, 255, 1);

  static final Color ENEMY_UNIT = Color.rgb(255, 0, 44, 0.4);

  static final Color SPELL_COLOR = Color.rgb(225, 210, 25, 1);

  static final Color defaultColor = Color.rgb(20, 255, 255, 0.5);

  static final ColorAdjust POSITIVE_BUFF_EFFECT = new ColorAdjust(-0.5, 1, -0.5, 0);
  static final ColorAdjust NEGATIVE_BUFF_EFFECT = new ColorAdjust(0.5, 1, -0.5, 0);

}
