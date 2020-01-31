package models.message;

import models.comperessedData.CompressedCard;

public class CardPositionMessage {
    private CompressedCard compressedCard;
    private CardPosition cardPosition;

    public CompressedCard getCompressedCard() {
        return compressedCard;
    }

    public CardPosition getCardPosition() {
        return cardPosition;
    }
}
