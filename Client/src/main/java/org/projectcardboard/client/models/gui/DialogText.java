package org.projectcardboard.client.models.gui;

import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import static org.projectcardboard.client.models.gui.UIConstants.SCALE;

public class DialogText extends Text {
  private static final Effect SHADOW = new DropShadow(20 * SCALE, Color.WHITE);
  private static final Font FONT = Font.font("SansSerif", FontWeight.EXTRA_BOLD, 35 * SCALE);

  public DialogText(String text) {
    super(text);
    setFont(FONT);
    setEffect(SHADOW);
  }
}
