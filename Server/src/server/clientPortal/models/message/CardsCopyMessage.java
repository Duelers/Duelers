package server.clientPortal.models.message;

import server.dataCenter.models.account.Collection;

class CardsCopyMessage {
    private final Collection cards;

    CardsCopyMessage(Collection cards) {
        this.cards = cards;
    }
}
