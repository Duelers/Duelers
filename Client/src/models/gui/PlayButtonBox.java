package models.gui;

import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static models.gui.UIConstants.SCALE;

class PlayButtonBox extends VBox {
    static final double BUTTON_WIDTH = 632 * SCALE;
    static final double BUTTON_HEIGHT = 1160 * SCALE;
    static final double PLATE_HEIGHT = 85 * SCALE;
    private static final Effect SHADOW = new DropShadow(40 * SCALE, Color.BLACK);
    private static Image PLATE_IMAGE;

    static {
        try {
            PLATE_IMAGE = new Image(new FileInputStream("Client/resources/menu/playButtons/panel_trim_plate.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    PlayButtonBox(PlayButtonItem item) {
        super(UIConstants.DEFAULT_SPACING * (-3));
        ImageView plate = ImageLoader.makeImageView(
                PLATE_IMAGE, BUTTON_WIDTH, PLATE_HEIGHT
        );

        StackPane imageZone = new PlayButtonImageZone(item);

        getChildren().addAll(imageZone, plate);
        setEffect(SHADOW);
    }
}
