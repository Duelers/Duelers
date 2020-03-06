package org.projectcardboard.client.models.message;

import shared.models.card.Card;

public class CardPositionMessage {
    private Card card;
    private CardPosition cardPosition;

    public Card getCard() {
        return card;
    }

    public CardPosition getCardPosition() {
        return cardPosition;
    }
}
