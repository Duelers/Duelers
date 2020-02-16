package server.dataCenter.models.db;

import server.dataCenter.DataBase;
import server.dataCenter.models.account.Collection;
import server.dataCenter.models.card.Card;

import java.util.ArrayList;
import java.util.List;

public class OldDataBase implements DataBase {
    private Collection originalCards = new Collection();
    private Collection newCustomCards = new Collection();
    private List<Card> collectibleItems = new ArrayList<>();
    private Card originalFlag;

    public Collection getOriginalCards() {
        return originalCards;
    }

    public Collection getNewCustomCards() {
        return newCustomCards;
    }

    public List<Card> getCollectibleItems() {
        return collectibleItems;
    }

    public Card getOriginalFlag() {
        return originalFlag;
    }

    @Override
    public void addNewCustomCards(Card card) {
        newCustomCards.addCard(card);
    }

    @Override
    public void removeCustomCards(Card card) {
        newCustomCards.removeCard(card);
    }

    @Override
    public void addOriginalCard(Card card) {
        originalCards.addCard(card);
    }

    @Override
    public void addNewCollectible(Card card) {
        collectibleItems.add(card);
    }

    @Override
    public void setOriginalFlag(Card originalFlag) {
        this.originalFlag = originalFlag;
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
