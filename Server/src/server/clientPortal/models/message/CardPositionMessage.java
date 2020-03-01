package server.clientPortal.models.message;

import shared.models.card.Card;
import shared.models.card.CompressedCard;

class CardPositionMessage {
    private CompressedCard compressedCard;
    private CardPosition cardPosition;

    CardPositionMessage(Card card, CardPosition cardPosition) {
        this.compressedCard = card.toCompressedCard();
        this.cardPosition = cardPosition;
    }
}
