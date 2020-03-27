package org.projectcardboard.client.models.gui;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Spinner;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class DefaultSpinner<T> extends Spinner<T> {
    private static final Background DEFAULT_BACKGROUND = new Background(
            new BackgroundFill(Color.rgb(70, 70, 70), CornerRadii.EMPTY, Insets.EMPTY)
    );
    private static final Border DEFAULT_BORDER = new Border(
            new BorderStroke(Color.gray(0.4), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)
    );
    private static final Insets DEFAULT_PADDING = new Insets(UIConstants.DEFAULT_SPACING * 2);

    public DefaultSpinner(ObservableList<T> list) {
        super(list);
        setGraphic();
    }

    public DefaultSpinner(int min, int max, int initialValue) {
        super(min, max, initialValue);
        setGraphic();
    }

    private void setGraphic() {
        getEditor().setFont(UIConstants.DEFAULT_FONT);
        getEditor().setPadding(DEFAULT_PADDING);
        getEditor().setStyle("-fx-text-inner-color: #fffbfd; -fx-background-color: rgb(70, 70, 70)");
        setBackground(DEFAULT_BACKGROUND);
        setBorder(DEFAULT_BORDER);
    }
}
