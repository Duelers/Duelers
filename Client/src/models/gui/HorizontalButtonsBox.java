
package models.gui;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;

import static models.gui.UIConstants.SCALE;

public class HorizontalButtonsBox extends HBox {
    private static final double BUTTON_WIDTH = 632 * SCALE;
    private static final double BUTTON_HEIGHT = 1160 * SCALE;

    public HorizontalButtonsBox(PlayButtonItem[] items) {
        super(UIConstants.DEFAULT_SPACING * 8);
        setAlignment(Pos.CENTER);
        setMaxHeight(PlayButtonBox.BUTTON_HEIGHT + PlayButtonBox.PLATE_HEIGHT);

        for (PlayButtonItem item : items) {
            PlayButtonBox buttonBox = new PlayButtonBox(item);
            getChildren().add(buttonBox);
        }
    }
}