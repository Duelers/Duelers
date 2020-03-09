package org.projectcardboard.client.models.gui;

import static org.projectcardboard.client.models.gui.UIConstants.SCALE;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import shared.models.card.CardType;
import shared.models.card.ICard;

class CardBackground extends StackPane {
    static final double CARD_WIDTH = 452 * SCALE;
    static final double GLOW_WIDTH = 506 * SCALE;
    static final double CARD_HEIGHT = 592 * SCALE;
    static final double GLOW_HEIGHT = 639 * SCALE;
    private static final HashMap<CardType, Image> background = new HashMap<>();
    private static Image glow;

    static {
        try {
            InputStream troopBackgroundR = CardBackground.class.getResourceAsStream("/card_backgrounds/troop.png");
            InputStream spellBackgroundR = CardBackground.class.getResourceAsStream("/card_backgrounds/spell.png");
            InputStream itemBackgroundR = CardBackground.class.getResourceAsStream("/card_backgrounds/item.png");
            InputStream glowR = CardBackground.class.getResourceAsStream("/card_backgrounds/glow.png");
            if (
                troopBackgroundR == null ||
                spellBackgroundR == null ||
                itemBackgroundR == null ||
                glowR == null
            ) {
                    throw new FileNotFoundException();
            }
            Image troopBackground = new Image(troopBackgroundR);
            Image spellBackground = new Image(spellBackgroundR);
            Image itemBackground = new Image(itemBackgroundR);
            glow = new Image(glowR);

            background.put(CardType.HERO, troopBackground);
            background.put(CardType.MINION, troopBackground);
            background.put(CardType.SPELL, spellBackground);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private final ImageView backgroundView;
    private final ImageView glowView;

    CardBackground(ICard card) {
        backgroundView = ImageLoader.makeImageView(background.get(card.getType()), CARD_WIDTH, CARD_HEIGHT);
        glowView = ImageLoader.makeImageView(glow, GLOW_WIDTH, GLOW_HEIGHT);
        glowView.setVisible(false);

        getChildren().addAll(backgroundView, glowView);
    }

    void showGlow() {
        glowView.setVisible(true);
    }

    void hideGlow() {
        glowView.setVisible(false);
    }

    void changeType(CardType newValue) {
        backgroundView.setImage(background.get(newValue));
    }
}
