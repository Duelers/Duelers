package server.dataCenter;

import server.dataCenter.models.account.Collection;
import server.dataCenter.models.card.Card;

import java.util.List;

public interface DataBase {

    Card getCard(String cardName);

    Collection getOriginalCards();

    List<Card> getCollectibleItems();


    Card getOriginalFlag();

    void addOriginalCard(Card card);

    void addNewCollectible(Card card);

    void setOriginalFlag(Card loadFile);

    boolean isEmpty();
}
