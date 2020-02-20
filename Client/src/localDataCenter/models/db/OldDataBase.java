package localDataCenter.models.db;

import localDataCenter.DataBase;
import localDataCenter.models.account.Collection;
import localDataCenter.models.card.Card;

public class OldDataBase implements DataBase {
    private Collection originalCards = new Collection();

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
