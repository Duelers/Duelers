package server.clientPortal.models.message;

import shared.models.card.CompressedCard;
import server.dataCenter.models.card.Card;

class CardPositionMessage {
    private CompressedCard compressedCard;
    private CardPosition cardPosition;

    CardPositionMessage(Card card, CardPosition cardPosition) {
        this.compressedCard = card.toCompressedCard();
        this.cardPosition = cardPosition;
    }
}
