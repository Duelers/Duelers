package org.projectcardboard.client.models.gui;

import javafx.scene.layout.GridPane;
import org.projectcardboard.client.models.account.Collection;
import shared.models.card.Card;

import java.util.ArrayList;
import java.util.List;

import static org.projectcardboard.client.models.gui.UIConstants.DEFAULT_SPACING;
import static org.projectcardboard.client.models.gui.UIConstants.SCALE;

public class CollectionCardsGrid extends GridPane {
  private static final int COLUMN_NUMBER = 5;
  private static final double WIDTH = 2350 * SCALE;

  public CollectionCardsGrid(Collection collection) {
    setHgap(DEFAULT_SPACING/2);
    setVgap(DEFAULT_SPACING * 1);
    setMinWidth(WIDTH/1.5);
    //setMaxWidth(WIDTH);

    ArrayList<Card> allCards = new ArrayList<>(collection.getHeroes());
    allCards.addAll(collection.getMinions());
    allCards.addAll(collection.getSpells());

    for (int i = 0; i < 10; i++) {
      final Card card = allCards.get(i);
      CardPane cardPane = new CardPane(card, false, false, null);
      add(cardPane, i % COLUMN_NUMBER, i / COLUMN_NUMBER);
    }
  }
}
