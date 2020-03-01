package models.message;


import shared.models.card.CompressedCard;

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
