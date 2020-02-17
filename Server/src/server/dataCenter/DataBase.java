package server.dataCenter;

import server.dataCenter.models.account.Collection;
import server.dataCenter.models.card.Card;

import java.util.List;

public interface DataBase {

    Card getCard(String cardName);

    Collection getOriginalCards();

    void addOriginalCard(Card card);

    boolean isEmpty();
}
