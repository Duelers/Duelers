package models.gui;

import controller.ShopController;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import models.card.Card;

import java.io.FileNotFoundException;
import java.util.List;

import static controller.SoundEffectPlayer.SoundName.select;
import static models.gui.UIConstants.DEFAULT_SPACING;
import static models.gui.UIConstants.SCALE;

public class ShopCardsGrid extends GridPane {
    private static final double BUTTONS_WIDTH = 506 * SCALE;
    private static final int COLUMN_NUMBER = 4;

    public ShopCardsGrid(List<Card> cards) throws FileNotFoundException {
        setHgap(DEFAULT_SPACING * 5);
        setVgap(DEFAULT_SPACING * 5);

        for (int i = 0; i < cards.size(); i++) {
            final Card card = cards.get(i);
            VBox shopCardBox = new VBox(-DEFAULT_SPACING);
            shopCardBox.setAlignment(Pos.CENTER);

            CardPane cardPane = new CardPane(card, true, true, null);

            HBox buttonsBox = new HBox(DEFAULT_SPACING,
                    new OrangeButton("BUY", event -> ShopController.getInstance().buy(card.getName()), select),
                    new OrangeButton("SELL", event -> ShopController.getInstance().sell(card.getName()), select)
            );
            buttonsBox.setMaxWidth(BUTTONS_WIDTH);

            shopCardBox.getChildren().addAll(cardPane, buttonsBox);

            add(
                    shopCardBox, i % COLUMN_NUMBER, i / COLUMN_NUMBER
            );
        }
    }
}
