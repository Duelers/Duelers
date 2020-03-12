package shared.models.game.availableactions;

import shared.models.card.Card;

public class BaseInsert<CardType extends Card> {
    private final CardType card;

    public BaseInsert(CardType card) {
        this.card = card;
    }

    public CardType getCard() {
        return card;
    }
}
