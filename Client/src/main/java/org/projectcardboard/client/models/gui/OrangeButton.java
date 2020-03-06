package org.projectcardboard.client.models.gui;

import org.projectcardboard.client.controller.SoundEffectPlayer;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

import static org.projectcardboard.client.controller.SoundEffectPlayer.SoundName;
import static org.projectcardboard.client.controller.SoundEffectPlayer.SoundName.hover;
import static org.projectcardboard.client.models.gui.UIConstants.SCALE;

public class OrangeButton extends Button {
    private static final Background DEFAULT_BACKGROUND = new Background(
            new BackgroundFill(
                    Color.rgb(250, 106, 54, 0.8), CornerRadii.EMPTY, Insets.EMPTY
            )
    );
    private static final Background HOVER_BACKGROUND = new Background(
            new BackgroundFill(
                    Color.rgb(227, 55, 60), CornerRadii.EMPTY, Insets.EMPTY
            )
    );
    private static final double WIDTH = 400 * SCALE;

    public OrangeButton(String text, EventHandler<? super MouseEvent> clickEvent, SoundName soundName) {
        super(text);
        setBackground(DEFAULT_BACKGROUND);
        setPadding(new Insets(UIConstants.DEFAULT_SPACING * 3));
        setPrefWidth(WIDTH);
        setFont(UIConstants.DEFAULT_FONT);
        setTextFill(Color.WHITE);

        setOnMouseEntered(event -> {
            setBackground(HOVER_BACKGROUND);
            SoundEffectPlayer.getInstance().playSound(hover);
            setCursor(UIConstants.SELECT_CURSOR);
        });

        setOnMouseExited(event -> {
            setBackground(DEFAULT_BACKGROUND);
            setCursor(UIConstants.DEFAULT_CURSOR);
        });

        setOnMouseClicked(event -> {
            SoundEffectPlayer.getInstance().playSound(soundName);
            clickEvent.handle(event);
        });
    }
}
