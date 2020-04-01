package org.projectcardboard.client.models.gui;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

class RotateAnimation {
  private final RotateTransition rotate;

  RotateAnimation(ImageView ringView) {
    rotate = new RotateTransition(Duration.seconds(1.5), ringView);
    rotate.setByAngle(360);
    rotate.setInterpolator(Interpolator.LINEAR);
    rotate.setCycleCount(Animation.INDEFINITE);
  }

  void play() {
    rotate.play();
  }

  void pause() {
    rotate.pause();
  }
}
