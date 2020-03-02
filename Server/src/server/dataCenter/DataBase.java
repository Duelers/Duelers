package server.dataCenter;

import server.dataCenter.models.account.Collection;
import shared.models.card.Card;


public interface DataBase {

    Card getCard(String cardName);

    Collection getOriginalCards();

    void addOriginalCard(Card card);

    boolean isEmpty();
}
