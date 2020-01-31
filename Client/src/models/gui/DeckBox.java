package models.gui;

import controller.CollectionMenuController;
import controller.SoundEffectPlayer;
import javafx.geometry.Insets;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import models.card.Deck;
import models.card.DeckExporter;
import view.CollectionMenu;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static models.gui.UIConstants.*;

public class DeckBox extends GridPane {
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
    private static final double ICON_SIZE = 50 * SCALE;
    private static final double WIDTH = SCENE_WIDTH * 0.18;
    private static final Insets PADDING = new Insets(30 * SCALE);
    private static final Font DETAILS_FONT = Font.font("DejaVu Sans Light", FontWeight.EXTRA_LIGHT, 20 * SCALE);
    private static final Effect ICON_SHADOW = new DropShadow(10 * SCALE, Color.WHITE);
    private static Image checkIcon;
    private static Image modifyIcon;
    private static Image removeIcon;
    private static Image saveIcon;
    private static Image mainIcon;
    private static Image disableMainIcon;

    static {
        try {
            checkIcon = new Image(new FileInputStream("Client/resources/ui/icon_check.png"));
            modifyIcon = new Image(new FileInputStream("Client/resources/ui/icon_modify.png"));
            removeIcon = new Image(new FileInputStream("Client/resources/ui/icon_remove.png"));
            saveIcon = new Image(new FileInputStream("Client/resources/ui/icon_save.png"));
            mainIcon = new Image(new FileInputStream("Client/resources/ui/icon_main_deck.png"));
            disableMainIcon = new Image(new FileInputStream("Client/resources/ui/icon_main_deck_disable.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public DeckBox(Deck deck) {
        setPadding(PADDING);
        setVgap(DEFAULT_SPACING * 2);
        setHgap(DEFAULT_SPACING * 2);
        setBackground(DEFAULT_BACKGROUND);
        setMinWidth(WIDTH);

        DefaultLabel deckName = new DefaultLabel(deck.getName().toUpperCase(), getFont(deck.getName()), Color.WHITE);
        ImageView modify = ImageLoader.makeImageView(modifyIcon, ICON_SIZE, ICON_SIZE);
        ImageView remove = ImageLoader.makeImageView(removeIcon, ICON_SIZE, ICON_SIZE);
        ImageView export = ImageLoader.makeImageView(saveIcon, ICON_SIZE, ICON_SIZE);
        DefaultLabel cardsNumber = new DefaultLabel(deck.getOthers().size() + " Cards", DETAILS_FONT, Color.WHITE);
        DefaultLabel itemsNumber = new DefaultLabel((deck.getItem() != null ? "1" : "0") + " Item", DETAILS_FONT, Color.WHITE);
        DefaultLabel heroNumber = new DefaultLabel((deck.getHero() != null ? "1" : "0") + " Hero", DETAILS_FONT, Color.WHITE);

        modify.setOnMouseEntered(event -> {
            modify.setEffect(ICON_SHADOW);
            SoundEffectPlayer.getInstance().playSound(SoundEffectPlayer.SoundName.hover);
            setCursor(SELECT_CURSOR);
        });
        modify.setOnMouseExited(event -> {
            modify.setEffect(null);
            setCursor(DEFAULT_CURSOR);
        });
        modify.setOnMouseClicked(event -> {
            SoundEffectPlayer.getInstance().playSound(SoundEffectPlayer.SoundName.click);
            CollectionMenu.getInstance().modify(deck);
        });

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
            CollectionMenuController.getInstance().removeDeck(deck.getName());
            try {
                CollectionMenu.getInstance().showCollectionCards();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });

        export.setOnMouseEntered(event -> {
            export.setEffect(ICON_SHADOW);
            SoundEffectPlayer.getInstance().playSound(SoundEffectPlayer.SoundName.hover);
            setCursor(SELECT_CURSOR);
        });
        export.setOnMouseExited(event -> {
            export.setEffect(null);
            setCursor(DEFAULT_CURSOR);
        });
        export.setOnMouseClicked(event -> {
            SoundEffectPlayer.getInstance().playSound(SoundEffectPlayer.SoundName.click);
            new DeckExporter(deck).export();
        });

        add(deckName, 5, 0, 4, 2);
        if (deck.isValid()) {
            ImageView valid = ImageLoader.makeImageView(checkIcon, ICON_SIZE, ICON_SIZE);
            add(valid, 12, 0, 1, 2);
        }

        if (deck.isMain()) {
            ImageView main = ImageLoader.makeImageView(mainIcon, ICON_SIZE, ICON_SIZE);
            add(main, 12, 2, 1, 1);
        } else {
            ImageView disableMain = ImageLoader.makeImageView(disableMainIcon, ICON_SIZE, ICON_SIZE);
            add(disableMain, 12, 2, 1, 1);

            disableMain.setOnMouseEntered(event -> {
                        disableMain.setEffect(new ColorAdjust(0, 0.5, 0, 0));
                        SoundEffectPlayer.getInstance().playSound(SoundEffectPlayer.SoundName.hover);
                        setCursor(SELECT_CURSOR);
                    }
            );
            disableMain.setOnMouseExited(event -> {
                disableMain.setEffect(null);
                setCursor(DEFAULT_CURSOR);
            });
            disableMain.setOnMouseClicked(event -> {
                SoundEffectPlayer.getInstance().playSound(SoundEffectPlayer.SoundName.click);
                CollectionMenuController.getInstance().selectDeck(deck.getName());
            });
        }

        add(heroNumber, 5, 2, 2, 1);
        add(itemsNumber, 7, 2);
        add(cardsNumber, 8, 2);

        add(modify, 0, 0);
        add(remove, 0, 1);
        add(export, 0, 2);

        setOnMouseEntered(event -> setBackground(HOVER_BACKGROUND));
        setOnMouseExited(event -> setBackground(DEFAULT_BACKGROUND));
    }

    private Font getFont(String name) {
        double size = Math.min(45 * SCALE, 45 * SCALE * 8 / name.length());
        return Font.font("DejaVu Sans Light", FontWeight.EXTRA_LIGHT, size);
    }
}
