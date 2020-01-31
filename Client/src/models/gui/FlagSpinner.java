package models.gui;

import javafx.geometry.Insets;
import javafx.scene.control.Spinner;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class FlagSpinner extends Spinner<Integer> {
    private static final Background BACKGROUND = new Background(
            new BackgroundFill(Color.rgb(70, 70, 70), CornerRadii.EMPTY, Insets.EMPTY)
    );
    private static final Border DEFAULT_BORDER = new Border(
            new BorderStroke(Color.gray(0.4), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)
    );
    private static final int MIN = 5;
    private static final int MAX = 30;
    private static final int INITIAL = 10;

    public FlagSpinner() {
        super(MIN, MAX, INITIAL);
        getEditor().setFont(UIConstants.DEFAULT_FONT);
        getEditor().setBorder(DEFAULT_BORDER);
        getEditor().setPadding(new Insets(UIConstants.DEFAULT_SPACING * 2));
        getEditor().setBackground(BACKGROUND);
        getEditor().setStyle("-fx-text-inner-color: #fffbfd;");

    }
}
