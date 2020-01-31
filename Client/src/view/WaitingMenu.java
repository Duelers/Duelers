package view;

import controller.GraphicalUserInterface;
import controller.WaitingController;
import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import models.gui.*;

import java.io.File;
import java.io.FileNotFoundException;

import static models.gui.UIConstants.*;

public class WaitingMenu extends Show {
    private static final Background ROOT_BACKGROUND = new Background(
            new BackgroundFill(
                    Color.rgb(40, 43, 53), CornerRadii.EMPTY, Insets.EMPTY
            )
    );
    private static final String backgroundUrl = "Client/resources/menu/background/wait_background.jpg";
    private static final double SPACE_HEIGHT = SCENE_HEIGHT / 2;
    private static final Image generalImage = ImageLoader.load("Client/resources/wait/general.png");
    private static final double HERO_WIDTH = 3800 * SCALE;
    private static final double HERO_HEIGHT = 2875 * SCALE;
    private static final Font FONT = Font.font("DejaVu Sans Light", FontWeight.EXTRA_LIGHT, 100 * SCALE);
    private static Media backgroundMusic = new Media(
            new File("Client/resources/music/waiting.m4a").toURI().toString()
    );
    private final Show previousShow;

    WaitingMenu(Show show) {
        this.previousShow = show;
        root.setBackground(ROOT_BACKGROUND);
        try {
            BorderPane background = BackgroundMaker.getPlayBackground(backgroundUrl);

            ImageView heroView = ImageLoader.makeImageView(generalImage, HERO_WIDTH, HERO_HEIGHT);
            heroView.relocate((SCENE_WIDTH - HERO_WIDTH) * 0.5, (SCENE_HEIGHT - HERO_HEIGHT) * 0.25);

            VBox container = new VBox(DEFAULT_SPACING * 2);
            container.setAlignment(Pos.BOTTOM_CENTER);
            container.setMinSize(SCENE_WIDTH, SCENE_HEIGHT * 0.9);

            DefaultLabel waitLabel = new DefaultLabel("WAITING...", FONT, Color.WHITE);
            addShadowAnimation(waitLabel);

            ImageButton cancelButton = new ImageButton("CANCEL", event -> cancel());

            container.getChildren().addAll(new Space(SPACE_HEIGHT), waitLabel, cancelButton);

            RadialVignette vignette = new RadialVignette(SCENE_WIDTH, SCENE_HEIGHT);

            AnchorPane sceneContents = new AnchorPane(background, heroView, vignette, container);
            root.getChildren().addAll(sceneContents);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void addShadowAnimation(DefaultLabel waitLabel) {
        AnimationTimer animation = new AnimationTimer() {
            private final double spread = 0.7;
            private final int red = 115;
            private final int green = 210;
            private final int blue = 255;
            private int currentOpacity = 50;
            private boolean ascending = false;

            @Override
            public void handle(long now) {
                if (ascending) {
                    if (currentOpacity >= 50) {
                        ascending = false;
                        return;
                    }
                    setEffect(++currentOpacity);
                } else {
                    if (currentOpacity <= 20) {
                        ascending = true;
                        return;
                    }
                    setEffect(--currentOpacity);
                }
            }

            private void setEffect(int opacity) {
                waitLabel.setEffect(
                        new DropShadow(
                                BlurType.THREE_PASS_BOX,
                                Color.rgb(red, green, blue, opacity * 0.02),
                                100 * SCALE, spread, 0, 0
                        )
                );
            }
        };
        animation.start();
    }

    private void cancel() {
        WaitingController.getInstance().cancel();
        close();
    }

    public void close() {
        previousShow.show();
    }

    @Override
    public void show() {
        super.show();
        GraphicalUserInterface.getInstance().setBackgroundMusic(backgroundMusic);
    }

    @Override
    public void showError(String message) {
        super.showError(message, "OK", event -> close());
    }
}
