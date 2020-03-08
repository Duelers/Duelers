package org.projectcardboard.client.models.gui;

import javafx.scene.layout.GridPane;
import shared.models.card.Card;

import java.io.FileNotFoundException;
import java.util.List;

import static org.projectcardboard.client.models.gui.UIConstants.DEFAULT_SPACING;
import static org.projectcardboard.client.models.gui.UIConstants.SCALE;

public class CollectionCardsGrid extends GridPane {
    private static final int COLUMN_NUMBER = 4;
    private static final double WIDTH = 2350 * SCALE;

    public CollectionCardsGrid(List<Card> cards) {
        setHgap(DEFAULT_SPACING * 5);
        setVgap(DEFAULT_SPACING * 5);
        setMinWidth(WIDTH);
        setMaxWidth(WIDTH);

        for (int i = 0; i < cards.size(); i++) {
            final Card card = cards.get(i);
            CardPane cardPane = new CardPane(card, false, false, null);
            add(
                    cardPane, i % COLUMN_NUMBER, i / COLUMN_NUMBER
            );
        }
    }
}
