package message;

import localDataCenter.models.account.Collection;

public class CardsCopyMessage {
    private Collection cards;

    CardsCopyMessage(Collection cards) {
        this.cards = cards;
    }

    public Collection getCards() {
        return cards;
    }

}
