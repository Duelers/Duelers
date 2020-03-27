package org.projectcardboard.client.models.gui;

import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import static org.projectcardboard.client.models.gui.UIConstants.SCALE;

class SearchField extends NormalField {
    private static final CornerRadii CORNER = new CornerRadii(20 * SCALE);
    private static final Background BACKGROUND = new Background(
            new BackgroundFill(Color.rgb(70, 70, 70, 0.6), CORNER, Insets.EMPTY)
    );
    private static final Border BORDER = new Border(
            new BorderStroke(Color.gray(0.4), BorderStrokeStyle.SOLID, CORNER, BorderWidths.DEFAULT)
    );
    private static final Insets PADDING = new Insets(UIConstants.DEFAULT_SPACING);
    private static final double WIDTH = 400 * SCALE;

    SearchField() {
        super("search cards", BACKGROUND, BORDER, PADDING);
        setMaxWidth(WIDTH);
    }
}
