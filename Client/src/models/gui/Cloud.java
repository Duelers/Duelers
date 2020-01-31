package models.gui;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Effect;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.io.FileNotFoundException;

import static models.gui.UIConstants.SCALE;

class Cloud extends Pane {
    private static final Effect EFFECT = new ColorAdjust(1, 0.9, -0.7, 0.6);
    private static final double WIDTH = 1400 * SCALE;
    private static final double HEIGHT = 750 * SCALE;
    private static final double DISTANCE = 800 * SCALE;
    private static final Duration DURATION = Duration.seconds(90);
    private TranslateTransition transition;

    Cloud() throws FileNotFoundException {
        for (int i = 0; i < 3; i++) {
            getChildren().add(ImageLoader.loadImage(
                    "Client/resources/particles/cloud_" + i + ".png",
                    WIDTH, HEIGHT, DISTANCE * i, 0
            ));
        }
        moveFog();
    }

    private void moveFog() {
        relocate(UIConstants.SCENE_WIDTH, UIConstants.SCENE_HEIGHT - HEIGHT * 0.8);
        setOpacity(0.95);
        setEffect(EFFECT);
        transition = new TranslateTransition(DURATION, this);
        transition.setByX(-UIConstants.SCENE_WIDTH * 2);
        transition.setInterpolator(Interpolator.LINEAR);
        transition.setCycleCount(Animation.INDEFINITE);

    }

    void play() {
        transition.play();
    }

    void pause() {
        transition.pause();
    }
}