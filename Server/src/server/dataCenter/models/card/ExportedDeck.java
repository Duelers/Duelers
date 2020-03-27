package server.dataCenter.models.card;

import shared.models.card.BaseExportedDeck;

public class ExportedDeck extends BaseExportedDeck<ServerCard, Deck> {
  public ExportedDeck(Deck deck) {
    super(deck);
  }
}
