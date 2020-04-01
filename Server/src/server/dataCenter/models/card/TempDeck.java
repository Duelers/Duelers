package server.dataCenter.models.card;

import shared.models.card.BaseTempDeck;

public class TempDeck extends BaseTempDeck<ServerCard, Deck> {
  public TempDeck(Deck deck) {
    super(deck);
  }
}
