package message;

import localGameCenter.models.compressedData.CompressedCard;
import localDataCenter.models.card.Card;

class CardPositionMessage {
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
