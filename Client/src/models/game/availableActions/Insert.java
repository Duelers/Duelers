package models.game.availableActions;


import shared.models.card.CompressedCard;

public class Insert {
    private CompressedCard card;

    Insert(CompressedCard card) {
        this.card = card;
    }

    public CompressedCard getCard() {
        return card;
    }
}
