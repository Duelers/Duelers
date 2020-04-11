package org.projectcardboard.client.models.gui;

import javafx.scene.layout.GridPane;
import org.projectcardboard.client.models.account.Collection;
import shared.models.card.Card;
import java.util.ArrayList;

import static org.projectcardboard.client.models.gui.UIConstants.DEFAULT_SPACING;
import static org.projectcardboard.client.models.gui.UIConstants.SCALE;

public class CollectionCardsGrid extends GridPane {
  private static final int COLUMN_NUMBER = 5;
  private static final double WIDTH = 2350 * SCALE;
  ArrayList<Card> allCards;
  private int currentIndex;
  private int numCardsOnScreen = 10;

  public CollectionCardsGrid(Collection collection) {
    setHgap(DEFAULT_SPACING / 2);
    setVgap(DEFAULT_SPACING * 1);
    setMinWidth(WIDTH / 1.5);
    // setMaxWidth(WIDTH);

    this.allCards = new ArrayList<>(collection.getHeroes());
    allCards.addAll(collection.getMinions());
    allCards.addAll(collection.getSpells());

    for (int i = 0; i < numCardsOnScreen; i++) {
      final Card card = allCards.get(i);
      CardPane cardPane = new CardPane(card, false, false, null);
      add(cardPane, i % COLUMN_NUMBER, i / COLUMN_NUMBER);
    }
    this.currentIndex = 10;
    setMaxHeight(getHeight());
  }

  public void nextPage() {
    if (currentIndex + numCardsOnScreen > allCards.size()) {
      System.out.println("next page will exceed deck size");
      return;
    }
    getChildren().clear();
    for (int i = currentIndex; i < currentIndex + numCardsOnScreen; i++) {
      final Card card = allCards.get(i);
      CardPane cardPane = new CardPane(card, false, false, null);
      add(cardPane, i % COLUMN_NUMBER, i / COLUMN_NUMBER);
    }
    this.currentIndex += numCardsOnScreen;
  }

  public void prevPage() {
    if (currentIndex - 10 < 0) {
      System.out.println("next page will exceed deck size");
      return;
    }
    this.currentIndex -= 10;
    getChildren().clear();

    for (int i = currentIndex; i < currentIndex + numCardsOnScreen; i++) {
      final Card card = allCards.get(i);
      CardPane cardPane = new CardPane(card, false, false, null);
      add(cardPane, i % COLUMN_NUMBER, i / COLUMN_NUMBER);
    }
  }
}
