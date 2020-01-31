package models.gui;

import controller.SoundEffectPlayer;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static controller.SoundEffectPlayer.SoundName;
import static models.gui.UIConstants.SCALE;

public class ImageButton extends StackPane {
    private static final Font FONT = Font.font("SansSerif", FontWeight.BOLD, 35 * SCALE);
    private static Image primaryDefault;
    private static Image primaryHover;

    static {
        try {
            primaryDefault = new Image(new FileInputStream("Client/resources/ui/button_primary@2x.png"));
            primaryHover = new Image(new FileInputStream("Client/resources/ui/button_primary_glow@2x.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public ImageButton(String text, EventHandler<? super MouseEvent> mouseEvent) {
        this(text, mouseEvent, primaryDefault, primaryHover);
    }

    public ImageButton(String text, EventHandler<? super MouseEvent> mouseEvent, Image defaultImage, Image hoverImage) {
        ImageView imageView = ImageLoader.makeImageView(defaultImage, defaultImage.getWidth() * SCALE, defaultImage.getHeight() * SCALE);
        DefaultLabel label = new DefaultLabel(text, FONT, Color.WHITE);

        setOnMouseEntered(event -> {
            imageView.setImage(hoverImage);
            SoundEffectPlayer.getInstance().playSound(SoundName.hover);
            setCursor(UIConstants.SELECT_CURSOR);
        });
        setOnMouseExited(event -> {
            imageView.setImage(defaultImage);
            setCursor(UIConstants.DEFAULT_CURSOR);
        });
        setOnMouseClicked(event -> {
            SoundEffectPlayer.getInstance().playSound(SoundEffectPlayer.SoundName.click);
            mouseEvent.handle(event);
        });

        getChildren().addAll(imageView, label);
    }
}
