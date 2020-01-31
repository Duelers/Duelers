package models.gui;

import controller.SoundEffectPlayer;
import javafx.event.EventHandler;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static controller.SoundEffectPlayer.SoundName;
import static models.gui.UIConstants.SCALE;

public class BackButton extends ImageView {
    private static final Effect HOVER_EFFECT = new ColorAdjust(0, 0.15, 0.2, 0);
    private static final double SIZE = 200 * SCALE;
    private static Image IMAGE;

    static {
        try {
            IMAGE = new Image(new FileInputStream("Client/resources/ui/button_back.png"));
        } catch (FileNotFoundException e) {
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
