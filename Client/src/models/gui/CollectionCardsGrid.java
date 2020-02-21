package models.gui;

import javafx.scene.layout.GridPane;
import server.dataCenter.models.card.Card;

import java.io.FileNotFoundException;
import java.util.List;

import static models.gui.UIConstants.DEFAULT_SPACING;
import static models.gui.UIConstants.SCALE;

public class CollectionCardsGrid extends GridPane {
    private static final int COLUMN_NUMBER = 4;
    private static final double WIDTH = 2350 * SCALE;

    public CollectionCardsGrid(List<Card> cards) throws FileNotFoundException {
        setHgap(DEFAULT_SPACING * 5);
        setVgap(DEFAULT_SPACING * 5);
        setMinWidth(WIDTH);
        setMaxWidth(WIDTH);

        for (int i = 0; i < cards.size(); i++) {
            final Card card = cards.get(i);
            CardPane cardPane = new CardPane(card, false, true, null);
            add(
                    cardPane, i % COLUMN_NUMBER, i / COLUMN_NUMBER
            );
        }
    }
}
