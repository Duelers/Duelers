package org.projectcardboard.client.models.card;

import shared.models.card.BaseExportedDeck;
import shared.models.card.Card;

public class ExportedDeck extends BaseExportedDeck<Card, Deck> {
    public ExportedDeck(Deck deck) {
        super(deck);
    }
}
