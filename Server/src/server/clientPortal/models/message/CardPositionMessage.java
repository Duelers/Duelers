package server.clientPortal.models.message;

import server.clientPortal.models.comperessedData.CompressedCard;
import server.dataCenter.models.card.Card;

public class CardPositionMessage {
    private CompressedCard compressedCard;
    private CardPosition cardPosition;

    CardPositionMessage(Card card, CardPosition cardPosition) {
        this.compressedCard = card.toCompressedCard();
        this.cardPosition = cardPosition;
    }

    public CompressedCard getCompressedCard() {
        return compressedCard;
    }

    public CardPosition getCardPosition() {
        return cardPosition;
    }
}
