package org.projectcardboard.client.models.gui;

import javafx.scene.paint.Color;

public class CombinedColor {
  private final Color color;

  public CombinedColor(Color first, Color second) {
    color = Color.rgb((int) (first.getRed() + second.getRed()) / 2,
        (int) (first.getGreen() + second.getGreen()) / 2,
        (int) (first.getBlue() + second.getBlue()) / 2);
  }

  public Color getColor() {
    return color;
  }
}
