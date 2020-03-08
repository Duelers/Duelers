package server.dataCenter;

import server.dataCenter.models.account.Collection;
import server.dataCenter.models.card.ServerCard;


public interface DataBase {

    ServerCard getCard(String cardName);

    Collection getOriginalCards();

    void addOriginalCard(ServerCard card);

    boolean isEmpty();
}
