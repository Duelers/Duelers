package localDataCenter;

import localDataCenter.models.account.Collection;
import localDataCenter.models.card.Card;


public interface DataBase {

    Card getCard(String cardName);

    Collection getOriginalCards();

    void addOriginalCard(Card card);

    boolean isEmpty();
}
