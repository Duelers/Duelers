package models.gui;

import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static models.gui.UIConstants.SCALE;

class MannaIcon extends StackPane {
    private static final Font FONT = Font.font("SansSerif", FontWeight.EXTRA_BOLD, 40 * SCALE);
    private static final double WIDTH = 135 * SCALE;
    private static final double HEIGHT = 148 * SCALE;
    private static Image mannaIcon;

    static {
        try {
            mannaIcon = new Image(new FileInputStream("Client/resources/ui/icon_mana@2x.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private final DefaultLabel label = new DefaultLabel("", FONT, Color.BLACK);

    MannaIcon(int mannaPoint) {
        label.setText(String.valueOf(mannaPoint));
        getChildren().addAll(ImageLoader.makeImageView(mannaIcon, WIDTH, HEIGHT), label);
    }

    public void setManna(int mannaPoint) {
        label.setText(String.valueOf(mannaPoint));
    }
}
