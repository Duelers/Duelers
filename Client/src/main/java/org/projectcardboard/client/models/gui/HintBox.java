package org.projectcardboard.client.models.gui;

import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import static org.projectcardboard.client.models.gui.UIConstants.SCALE;

class HintBox extends VBox {
    private static final Background MENU_HINT_BACKGROUND = new Background(
            new BackgroundFill(
                    Color.rgb(40, 40, 40, 0.7),
                    new CornerRadii(UIConstants.DEFAULT_SPACING), new Insets(-UIConstants.DEFAULT_SPACING)
            )
    );
    private static final Font FONT = Font.font("SansSerif", FontWeight.BOLD, 25 * SCALE);
    private static final double WIDTH = 250 * SCALE;

    HintBox(String hintText) {
        getChildren().add(makeHintText(hintText));
        setPadding(new Insets(UIConstants.DEFAULT_SPACING));
        setBackground(MENU_HINT_BACKGROUND);
        setVisible(false);
    }

    private Text makeHintText(String text) {
        return new DefaultText(
                text, WIDTH, FONT, Color.WHITE
        );
    }
}
