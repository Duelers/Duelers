package org.projectcardboard.client.models.gui;

import org.projectcardboard.client.controller.SoundEffectPlayer;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.effect.Effect;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.projectcardboard.client.models.gui.UIConstants.SCALE;

public class DialogContainer extends BorderPane {
    private static final Background BACKGROUND = new Background(
            new BackgroundFill(Color.rgb(0, 0, 0, 0.4), CornerRadii.EMPTY, Insets.EMPTY));
    private static final Effect BACKGROUND_BLUR = new GaussianBlur(30 * SCALE);
    private final AnchorPane root;

    public DialogContainer(AnchorPane root, Node center) {
        this.root = root;
        setMinSize(UIConstants.SCENE_WIDTH, UIConstants.SCENE_HEIGHT);
        setBackground(BACKGROUND);
        setCenter(center);
    }

    void makeClosable(AtomicBoolean shouldBeClosed, EventHandler<InputEvent> e) {
        setOnMouseClicked(event -> {
            if (!shouldBeClosed.get()) {
                shouldBeClosed.set(true);
                return;
            }
            e.handle(event);
            close();
        });
        setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ESCAPE)) {
                e.handle(event);
                close();
            }
        });
    }

    public void show() {
        root.getChildren().get(0).setEffect(BACKGROUND_BLUR);
        SoundEffectPlayer.getInstance().playSound(SoundEffectPlayer.SoundName.open_dialog);
        root.getChildren().add(this);
    }

    public void close() {
        root.getChildren().remove(this);
        root.getChildren().get(0).setEffect(null);
        SoundEffectPlayer.getInstance().playSound(SoundEffectPlayer.SoundName.close_dialog);
    }
}
