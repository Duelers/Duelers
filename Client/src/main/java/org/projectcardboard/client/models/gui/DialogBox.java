package org.projectcardboard.client.models.gui;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.input.InputEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.projectcardboard.client.controller.SoundEffectPlayer.SoundName.click;
import static org.projectcardboard.client.models.gui.UIConstants.SCALE;

public class DialogBox extends VBox {
    private static final CornerRadii DEFAULT_CORNER_RADIUS = new CornerRadii(UIConstants.DEFAULT_SPACING * 3);
    private static final Background DIALOG_BACKGROUND = new Background(
            new BackgroundFill(
                    Color.rgb(255, 200, 200, 0.6), DEFAULT_CORNER_RADIUS, Insets.EMPTY
            )
    );
    private static final Effect BOX_SHADOW = new DropShadow(40 * SCALE, Color.BLACK);
    private static final double MAX_WIDTH = 1200 * SCALE;
    private static final double MAX_HEIGHT = 700 * SCALE;

    public DialogBox(Node... children) {
        super(UIConstants.DEFAULT_SPACING * 4, children);
        setAlignment(Pos.CENTER);
        setPadding(new Insets(UIConstants.DEFAULT_SPACING * 6));
        setBackground(DIALOG_BACKGROUND);
        setMaxSize(MAX_WIDTH, MAX_HEIGHT);
        setEffect(BOX_SHADOW);
    }

    public void makeButton(String text, EventHandler<? super MouseEvent> event) {
        getChildren().add(new OrangeButton(text, event, click, false));
    }

    private void preventClosingOnClick(AtomicBoolean shouldBeClosed) {
        setOnMouseClicked(event -> shouldBeClosed.set(false));
    }

    public void makeClosable(DialogContainer dialogContainer) {
        makeClosable(dialogContainer, closeEvent -> {
        });
    }


    public void makeClosable(DialogContainer dialogContainer, EventHandler<InputEvent> closeEvent) {
        AtomicBoolean shouldBeClosed = new AtomicBoolean(true);
        dialogContainer.makeClosable(shouldBeClosed, closeEvent);
        preventClosingOnClick(shouldBeClosed);
    }
}
