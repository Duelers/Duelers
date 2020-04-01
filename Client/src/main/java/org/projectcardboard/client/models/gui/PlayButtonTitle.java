package org.projectcardboard.client.models.gui;

import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import static org.projectcardboard.client.models.gui.UIConstants.SCALE;

class PlayButtonTitle extends Label {
  private static final Font FONT = Font.font("SansSerif", FontWeight.BOLD, 55 * SCALE);

  PlayButtonTitle(String title) {
    super(title);
    setFont(FONT);
    setTextFill(Color.WHITE);
  }
}
