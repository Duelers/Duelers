package org.projectcardboard.client.models.gui;

import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static org.projectcardboard.client.models.gui.UIConstants.SCALE;

class ManaIcon extends StackPane {
    private static final Font FONT = Font.font("SansSerif", FontWeight.EXTRA_BOLD, 40 * SCALE);
    private static final double WIDTH = 135 * SCALE;
    private static final double HEIGHT = 148 * SCALE;
    private static Image manaIcon;

    static {
        try {
            manaIcon = new Image(new FileInputStream("Client/src/main/resources/ui/icon_mana@2x.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private final DefaultLabel label = new DefaultLabel("", FONT, Color.BLACK);

    ManaIcon(int manaCost) {
        label.setText(String.valueOf(manaCost));
        getChildren().addAll(ImageLoader.makeImageView(manaIcon, WIDTH, HEIGHT), label);
    }

    public void setMana(int manaCost) {
        label.setText(String.valueOf(manaCost));
    }
}
