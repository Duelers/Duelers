package models.gui;

import controller.SoundEffectPlayer;
import javafx.geometry.Insets;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import models.card.EditableCard;
import models.card.spell.Spell;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static models.gui.UIConstants.*;

public class SpellBox extends HBox {
    private static final Background DEFAULT_BACKGROUND = new Background(
            new BackgroundFill(
                    Color.rgb(43, 57, 69), CornerRadii.EMPTY, Insets.EMPTY
            )
    );
    private static final Background HOVER_BACKGROUND = new Background(
            new BackgroundFill(
                    Color.rgb(49, 68, 82), CornerRadii.EMPTY, Insets.EMPTY
            )
    );
    private static final double WIDTH = 100 * SCALE;
    private static final double ICON_SIZE = 50 * SCALE;
    private static final Insets PADDING = new Insets(30 * SCALE);
    private static final Effect ICON_SHADOW = new DropShadow(10 * SCALE, Color.WHITE);
    private static Image removeIcon;

    static {
        try {
            removeIcon = new Image(new FileInputStream("Client/resources/ui/icon_remove.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public SpellBox(EditableCard card, Spell spell) {
        super(DEFAULT_SPACING * 2);
        setBackground(DEFAULT_BACKGROUND);
        setMinWidth(WIDTH);
        setPadding(PADDING);
        DefaultLabel id = new DefaultLabel(spell.getSpellId(), DEFAULT_FONT, Color.WHITE);
        ImageView remove = ImageLoader.makeImageView(removeIcon, ICON_SIZE, ICON_SIZE);

        remove.setOnMouseEntered(event -> {
            remove.setEffect(ICON_SHADOW);
            SoundEffectPlayer.getInstance().playSound(SoundEffectPlayer.SoundName.hover);
            setCursor(SELECT_CURSOR);
        });
        remove.setOnMouseExited(event -> {
            remove.setEffect(null);
            setCursor(DEFAULT_CURSOR);
        });
        remove.setOnMouseClicked(event -> {
            SoundEffectPlayer.getInstance().playSound(SoundEffectPlayer.SoundName.click);
            card.removeSpell(spell);
        });

        setOnMouseEntered(event -> setBackground(HOVER_BACKGROUND));
        setOnMouseExited(event -> setBackground(DEFAULT_BACKGROUND));

        getChildren().addAll(id, remove);
    }
}
