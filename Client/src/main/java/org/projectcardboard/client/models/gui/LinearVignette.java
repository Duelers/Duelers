package org.projectcardboard.client.models.gui;

import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;

class LinearVignette extends Parent {
  private static final double OPACITY = 0.5;

  LinearVignette(double width, double height) {
    Rectangle region = new Rectangle(width, height);
    region
        .setFill(new LinearGradient(0, 0, 0, region.getHeight() * 0.95, false, CycleMethod.NO_CYCLE,
            new Stop(0, Color.TRANSPARENT), new Stop(1, Color.color(0, 0, 0, OPACITY))));
    getChildren().add(region);
  }
}

