package models.game.availableActions;


import shared.models.card.Card;

public class Insert {
    private final Card card;

    Insert(Card card) {
        this.card = card;
    }

    public Card getCard() {
        return card;
    }
}
