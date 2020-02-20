package localGameCenter.models.game.availableActions;


import localDataCenter.models.card.Card;

public class Insert {
    private Card card;

    Insert(Card card) {
        this.card = card;
    }

    public Card getCard() {
        return card;
    }
}
