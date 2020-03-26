package org.projectcardboard.client.models.gui;

import javafx.geometry.Insets;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class NormalField extends TextField {
    private static final Background DEFAULT_BACKGROUND = new Background(
            new BackgroundFill(Color.rgb(70, 70, 70), CornerRadii.EMPTY, Insets.EMPTY));
    private static final Border DEFAULT_BORDER = new Border(
            new BorderStroke(Color.gray(0.4), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT));
    private static final Insets DEFAULT_PADDING = new Insets(UIConstants.DEFAULT_SPACING * 2);

    public NormalField(String text) {
        this(text, DEFAULT_BACKGROUND, DEFAULT_BORDER, DEFAULT_PADDING);
    }

    NormalField(String text, Background background, Border border, Insets padding) {
        setFont(UIConstants.DEFAULT_FONT);
        setBackground(background);
        setBorder(border);
        setPadding(padding);
        setPromptText(text);
        setStyle("-fx-text-inner-color: #fffbfd;");
    }
}
