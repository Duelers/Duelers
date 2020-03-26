package org.projectcardboard.client.models.gui;

import javafx.geometry.Orientation;
import javafx.scene.control.Separator;

public class DefaultSeparator extends Separator {
  private static final double SEPARATOR_OPACITY = 0.3;

  public DefaultSeparator(Orientation orientation) {
    this(orientation, SEPARATOR_OPACITY);
  }

  DefaultSeparator(Orientation orientation, double opacity) {
    super(orientation);
    setOpacity(opacity);
  }
}
