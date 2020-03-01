package server.clientPortal.models.message;

import shared.models.card.Card;

class CardPositionMessage {
    private Card card;
    private CardPosition cardPosition;

    CardPositionMessage(Card card, CardPosition cardPosition) {
        this.card = card;
        this.cardPosition = cardPosition;
    }
}
