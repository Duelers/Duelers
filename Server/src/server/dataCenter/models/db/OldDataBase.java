package server.dataCenter.models.db;

import server.dataCenter.DataBase;
import server.dataCenter.models.account.Collection;
import shared.models.card.Card;

public class OldDataBase implements DataBase {
    private final Collection originalCards = new Collection();

    public Collection getOriginalCards() {
        return originalCards;
    }


    @Override
    public void addOriginalCard(Card card) {
        originalCards.addCard(card);
    }

    @Override
    public boolean isEmpty() {
        return true;
    }


    @Override
    public Card getCard(String cardName) {
        return null;
    }
}
