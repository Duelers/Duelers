package org.projectcardboard.client.models.card;

import shared.models.card.BaseTempDeck;
import shared.models.card.Card;

public class TempDeck extends BaseTempDeck<Card, Deck> {
  public TempDeck(Deck deck) {
    super(deck);
  }
}
