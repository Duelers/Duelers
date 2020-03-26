package org.projectcardboard.client.models.gui;

import static org.projectcardboard.client.models.gui.UIConstants.SCALE;

import java.io.FileNotFoundException;
import java.io.InputStream;

import org.projectcardboard.client.controller.SoundEffectPlayer;
import org.projectcardboard.client.controller.SoundEffectPlayer.SoundName;

import javafx.event.EventHandler;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class BackButton extends ImageView {
    private static final Effect HOVER_EFFECT = new ColorAdjust(0, 0.15, 0.2, 0);
    private static final double SIZE = 200 * SCALE;
    private static Image IMAGE;

    static {
        try {
            InputStream backButtonResource = BackButton.class.getResourceAsStream("/ui/button_back.png");
            if (backButtonResource == null) {
                throw new FileNotFoundException();
            }
            IMAGE = new Image(backButtonResource);
        } catch (FileNotFoundException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    public BackButton(EventHandler<? super MouseEvent> clickEvent) {
        super(IMAGE);
        setFitWidth(SIZE);
        setFitHeight(SIZE);

        setOnMouseEntered(event -> {
            setEffect(HOVER_EFFECT);
            SoundEffectPlayer.getInstance().playSound(SoundName.hover);
            setCursor(UIConstants.SELECT_CURSOR);
        });

        setOnMouseExited(event -> {
            setEffect(null);
            setCursor(UIConstants.DEFAULT_CURSOR);
        });

        setOnMouseClicked(event -> {
            SoundEffectPlayer.getInstance().playSound(SoundName.exit_page);
            clickEvent.handle(event);
        });
    }
}
