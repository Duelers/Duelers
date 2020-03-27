package server.dataCenter.models.db;

import server.dataCenter.DataBase;
import server.dataCenter.models.account.Collection;
import server.dataCenter.models.card.ServerCard;

public class OldDataBase implements DataBase {
    private final Collection originalCards = new Collection();

    public Collection getOriginalCards() {
        return originalCards;
    }


    @Override
    public void addOriginalCard(ServerCard card) {
        originalCards.addCard(card);
    }

    @Override
    public boolean isEmpty() {
        return true;
    }


    @Override
    public ServerCard getCard(String cardName) {
        return null;
    }
}
