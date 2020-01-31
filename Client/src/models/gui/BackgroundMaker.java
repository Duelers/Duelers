package models.gui;

import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

import java.io.FileNotFoundException;
import java.util.HashMap;

import static models.gui.UIConstants.SCALE;

public class BackgroundMaker {
    private static final String BACKGROUND_URL = "Client/resources/menu/background/background.jpg";
    private static final String FOREGROUND_URL = "Client/resources/menu/background/foreground.png";
    private static final String FAR_PILLAR_URL = "Client/resources/menu/background/far_pillars.png";
    private static final String NEAR_PILLAR_URL = "Client/resources/menu/background/near_pillars.png";
    private static final String VIGNETTE_URL = "Client/resources/menu/background/vignette.png";
    private static final double FOREGROUND_WIDTH = 2400 * SCALE;
    private static final double FOREGROUND_HEIGHT = 726 * SCALE;
    private static final double NEAR_PILLARS_WIDTH = 2000 * SCALE;
    private static final double NEAR_PILLARS_HEIGHT = 1700 * SCALE;
    private static final double FAR_PILLARS_WIDTH = 1772 * SCALE;
    private static final double FAR_PILLARS_HEIGHT = 1342 * SCALE;
    private static final double VIGNETTE_WIDTH = 1429 * SCALE;
    private static final double VIGNETTE_HEIGHT = 1400 * SCALE;
    private static final double BACKGROUND_BLUR = 50 * SCALE;
    private static final HashMap<String, BorderPane> playBackgroundsByUrl = new HashMap<>();
    private static BorderPane menuBackground;

    private static void makeMenuBackground() throws FileNotFoundException {
        menuBackground = new BorderPane();

        ImageView backgroundView = ImageLoader.loadImage(BACKGROUND_URL, UIConstants.SCENE_WIDTH, UIConstants.SCENE_HEIGHT);
        ImageView foregroundView = ImageLoader.loadImage(
                FOREGROUND_URL, FOREGROUND_WIDTH, FOREGROUND_HEIGHT,
                UIConstants.SCENE_WIDTH - FOREGROUND_WIDTH,
                UIConstants.SCENE_HEIGHT - FOREGROUND_HEIGHT
        );
        ImageView farPillarsView = ImageLoader.loadImage(
                FAR_PILLAR_URL, FAR_PILLARS_WIDTH, FAR_PILLARS_HEIGHT,
                0, UIConstants.SCENE_HEIGHT - FAR_PILLARS_HEIGHT
        );
        ImageView nearPillarsView = ImageLoader.loadImage(
                NEAR_PILLAR_URL, NEAR_PILLARS_WIDTH, NEAR_PILLARS_HEIGHT,
                0, UIConstants.SCENE_HEIGHT - NEAR_PILLARS_HEIGHT
        );
        ImageView vignetteView = ImageLoader.loadImage(VIGNETTE_URL, VIGNETTE_WIDTH, VIGNETTE_HEIGHT);

        Cloud cloud = new Cloud();

        menuBackground.getChildren().addAll(backgroundView, farPillarsView, nearPillarsView, cloud, foregroundView, vignetteView);
    }

    private static void makePlayBackground(String url) throws FileNotFoundException {
        BorderPane background = new BorderPane();

        ImageView backgroundView = ImageLoader.loadImage(url, UIConstants.SCENE_WIDTH, UIConstants.SCENE_HEIGHT);

        backgroundView.setEffect(new GaussianBlur(BACKGROUND_BLUR / 3));
        background.setEffect(new ColorAdjust(0, 0, -0.1, 0));

        background.getChildren().addAll(backgroundView);
        playBackgroundsByUrl.put(url, background);
    }

    public static BorderPane getPlayBackground(String url) throws FileNotFoundException {
        if (playBackgroundsByUrl.get(url) == null) {
            makePlayBackground(url);
        }
        return playBackgroundsByUrl.get(url);
    }

    public static BorderPane getMenuBackground() throws FileNotFoundException {
        if (menuBackground == null) {
            makeMenuBackground();
        }
        return menuBackground;
    }

    public static void makeMenuBackgroundFrozen() {
        menuBackground.setEffect(new GaussianBlur(BACKGROUND_BLUR));
        menuBackground.getChildren().stream().filter(node -> node instanceof Cloud).forEach(node -> ((Cloud) node).pause());
    }

    public static void makeMenuBackgroundUnfrozen() {
        menuBackground.setEffect(null);
        menuBackground.getChildren().stream().filter(node -> node instanceof Cloud).forEach(node -> ((Cloud) node).play());
    }
}
