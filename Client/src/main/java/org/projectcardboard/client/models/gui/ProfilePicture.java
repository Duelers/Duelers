package org.projectcardboard.client.models.gui;

import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import static org.projectcardboard.client.models.gui.UIConstants.SCALE;

class ProfilePicture extends Rectangle {
  private static final double CORNER_RADIUS = 30 * SCALE;
  private static final double SIZE = 600 * SCALE;
  private static final Effect SHADOW = new DropShadow(30 * SCALE, Color.BLACK);

  ProfilePicture(Image image) {
    super(SIZE, SIZE);
    setArcWidth(CORNER_RADIUS);
    setArcHeight(CORNER_RADIUS);
    setFill(new ImagePattern(image));
    setEffect(SHADOW);
  }
}
