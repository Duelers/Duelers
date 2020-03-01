package models.gui;

import controller.CollectionMenuController;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import shared.models.card.ICard;
import models.card.Deck;

import java.io.FileNotFoundException;
import java.util.List;

import static controller.SoundEffectPlayer.SoundName.select;
import static models.gui.UIConstants.DEFAULT_SPACING;
import static models.gui.UIConstants.SCALE;

public class DeckCardsGrid extends GridPane {
    private static final double BUTTONS_WIDTH = 506 * SCALE;
    private static final int COLUMN_NUMBER = 4;
    private static final double WIDTH = 2350 * SCALE;

    public DeckCardsGrid(List<? extends ICard> cards, Deck deck) throws FileNotFoundException {
        setHgap(DEFAULT_SPACING * 5);
        setVgap(DEFAULT_SPACING * 5);
        setMinWidth(WIDTH);
        setMaxWidth(WIDTH);
        for (int i = 0; i < cards.size(); i++) {
            final ICard card = cards.get(i);
            VBox deckCardBox = new VBox(-DEFAULT_SPACING);
            deckCardBox.setAlignment(Pos.CENTER);

            DeckCardPane cardPane = new DeckCardPane(card, deck);

            HBox buttonsBox = new HBox(UIConstants.DEFAULT_SPACING,
                    new OrangeButton("ADD", event ->
                            CollectionMenuController.getInstance().addCardToDeck(cardPane.getDeck(), card.getName()),
                            select
                    ),
                    new OrangeButton("REMOVE", event ->
                            CollectionMenuController.getInstance().removeCardFromDeck(cardPane.getDeck(), card.getName()),
                            select
                    )
            );

            buttonsBox.setMaxWidth(BUTTONS_WIDTH);

            deckCardBox.getChildren().addAll(cardPane, buttonsBox);

            add(
                    deckCardBox, i % COLUMN_NUMBER, i / COLUMN_NUMBER
            );
        }
    }
}
