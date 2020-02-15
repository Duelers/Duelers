package server.dataCenter;

import server.dataCenter.models.account.Collection;
import server.dataCenter.models.card.Card;
import server.gameCenter.models.game.Story;

import java.util.List;

public interface DataBase {

    Card getCard(String cardName);

    Collection getOriginalCards();

    List<Story> getStories();

    List<Card> getCollectibleItems();


    Card getOriginalFlag();

    void addOriginalCard(Card card);

    void addNewCollectible(Card card);

    void setOriginalFlag(Card loadFile);

    void addStory(Story story);

    boolean isEmpty();
}
