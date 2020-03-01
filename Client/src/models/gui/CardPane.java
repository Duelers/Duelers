package models.gui;

import controller.Client;
import javafx.application.Platform;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import shared.models.card.ICard;
import models.account.Collection;
import models.card.Deck;
import shared.models.card.CardType;
import view.BattleView.CardAnimation;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileNotFoundException;

import static models.gui.CardBackground.*;
import static models.gui.CardDetailBox.DESCRIPTION_COLOR;
import static models.gui.CardDetailBox.DESCRIPTION_FONT;
import static models.gui.UIConstants.SCALE;

public class CardPane extends AnchorPane implements PropertyChangeListener {
    private static final double AP_X = 120 * SCALE;
    private static final double HP_X = 360 * SCALE;
    private static final double AP_HP_Y = 360 * SCALE;
    private static final Font AP_HP_FONT = Font.font("SansSerif", FontWeight.SEMI_BOLD, 30 * SCALE);
    private static final double SPRITE_CENTER_X = GLOW_WIDTH / 2;
    private static final double SPRITE_CENTER_Y = 180 * SCALE;
    private final CardBackground background;
    private final CardDetailBox detailBox;
    private final CardAnimation cardAnimation;
    private final DefaultLabel apLabel;
    private final DefaultLabel hpLabel;
    private final ManaIcon manaPane;
    private final PriceBox priceBox;

    Deck deck;
    ICard card;
    DefaultLabel countLabel;
    int oldCount;

    public CardPane(ICard card, boolean showPrice, boolean showCount, Deck deck) throws FileNotFoundException {
        this.card = card;
        this.deck = deck;
        setPrefSize(GLOW_WIDTH, GLOW_HEIGHT);

        background = new CardBackground(card);

        detailBox = new CardDetailBox(card);
        getChildren().addAll(background, detailBox);

        apLabel = new DefaultLabel(
                String.valueOf(card.getDefaultAp()), AP_HP_FONT, Color.WHITE, AP_X, AP_HP_Y
        );
        hpLabel = new DefaultLabel(
                String.valueOf(card.getDefaultHp()), AP_HP_FONT, Color.WHITE, HP_X, AP_HP_Y
        );
        if (card.getType() == CardType.HERO || card.getType() == CardType.MINION) {
            getChildren().addAll(apLabel, hpLabel);
        }

        manaPane = new ManaIcon(card.getManaCost());
        if (card.getType() == CardType.SPELL || card.getType() == CardType.MINION) {
            getChildren().add(manaPane);
        }

        priceBox = new PriceBox(card.getPrice());
        if (showPrice) {
            getChildren().add(priceBox);
        }

        if (showCount) {
            count(card);
            showCount();
        }

        cardAnimation = new CardAnimation(this, card, SPRITE_CENTER_Y, SPRITE_CENTER_X);

        setOnMouseEntered(event -> {
            cardAnimation.inActive();
            background.showGlow();
            setCursor(UIConstants.SELECT_CURSOR);
        });
        setOnMouseExited(event -> {
            cardAnimation.pause();
            background.hideGlow();
            setCursor(UIConstants.DEFAULT_CURSOR);
        });
    }

    void count(ICard card) {
        Client.getInstance().getAccount().addPropertyChangeListener(this);
        oldCount = Client.getInstance().getAccount().getCollection().count(card.getName());
    }

    private void showCount() {
        countLabel = new DefaultLabel(
                "X " + oldCount,
                DESCRIPTION_FONT, DESCRIPTION_COLOR
        );

        countLabel.relocate(CARD_WIDTH * 0.53, CARD_HEIGHT * 0.98);
        getChildren().add(countLabel);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("collection")) {
            int newCount = ((Collection) evt.getNewValue()).count(card.getName());
            if (newCount != oldCount) {
                oldCount = newCount;
                Platform.runLater(() ->
                        countLabel.setText("X " + newCount)
                );
            }
        }
    }

    void setName(String newValue) {
        detailBox.setName(newValue);
    }

    void setType(CardType newValue) {
        background.changeType(newValue);
        detailBox.setType(newValue);
        switch (newValue) {
            case HERO:
                getChildren().remove(manaPane);
                if (!getChildren().contains(apLabel)) {
                    getChildren().addAll(apLabel, hpLabel);
                }
                break;
            case SPELL:
                getChildren().removeAll(apLabel, hpLabel);
                if (!getChildren().contains(manaPane)) {
                    getChildren().add(manaPane);
                }
                break;
            case MINION:
                if (!getChildren().contains(apLabel)) {
                    getChildren().addAll(apLabel, hpLabel);
                }
                if (!getChildren().contains(manaPane)) {
                    getChildren().add(manaPane);
                }
        }
    }

    void setDescription(String newValue) {
        detailBox.setDescription(newValue);
    }

    void updateSprite() {
        cardAnimation.setSprite(card);
    }

    void setDefaultAp(int newValue) {
        apLabel.setText(String.valueOf(newValue));
    }

    void setDefaultHp(int newValue) {
        hpLabel.setText(String.valueOf(newValue));
    }

    void setManaCost(int newValue) {
        manaPane.setMana(newValue);
    }

    void setPrice(int newValue) {
        priceBox.setPrice(newValue);
    }
}