package org.projectcardboard.client.models.gui;

import org.projectcardboard.client.controller.SoundEffectPlayer;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import static org.projectcardboard.client.models.gui.UIConstants.SCALE;


class SearchButton extends Button {
    private static final CornerRadii CORNER = new CornerRadii(20 * SCALE);
    private static final Background DEFAULT_BACKGROUND = new Background(
            new BackgroundFill(Color.rgb(70, 70, 70, 0.6), CORNER, Insets.EMPTY)
    );
    private static final Background HOVER_BACKGROUND = new Background(
            new BackgroundFill(Color.rgb(50, 50, 50, 0.8), CORNER, Insets.EMPTY)
    );
    private static final Border BORDER = new Border(
            new BorderStroke(Color.gray(0.4), BorderStrokeStyle.SOLID, CORNER, BorderWidths.DEFAULT)
    );
    private static final Insets PADDING = new Insets(UIConstants.DEFAULT_SPACING);

    SearchButton(EventHandler<? super MouseEvent> mouseEvent) {
        super("search");
        setBackground(DEFAULT_BACKGROUND);
        setBorder(BORDER);
        setFont(UIConstants.DEFAULT_FONT);
        setPadding(PADDING);
        setTextFill(Color.WHITE);

        setOnMouseEntered(event -> {
            setBackground(HOVER_BACKGROUND);
            SoundEffectPlayer.getInstance().playSound(SoundEffectPlayer.SoundName.hover);
            setCursor(UIConstants.SELECT_CURSOR);
        });

        setOnMouseExited(event -> {
            setBackground(DEFAULT_BACKGROUND);
            setCursor(UIConstants.DEFAULT_CURSOR);
        });

        setOnMouseClicked(event -> {
            SoundEffectPlayer.getInstance().playSound(SoundEffectPlayer.SoundName.click);
            mouseEvent.handle(event);
        });
    }
}
