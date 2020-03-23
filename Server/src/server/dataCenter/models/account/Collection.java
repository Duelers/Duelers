package server.dataCenter.models.account;

import server.GameServer;
import server.dataCenter.DataCenter;
import server.dataCenter.models.card.Deck;
import server.dataCenter.models.card.ExportedDeck;
import server.dataCenter.models.card.ServerCard;
import server.exceptions.ClientException;
import server.exceptions.LogicException;
import shared.models.account.BaseCollection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Collection extends BaseCollection<ServerCard> {

    boolean hasCard(String cardId) {
        return hasCard(cardId, heroes) || hasCard(cardId, minions) || hasCard(cardId, spells);
    }

    private boolean hasCard(String cardId, List<ServerCard> cards) {
        if (cardId == null || cards == null)
            return false;
        for (ServerCard card : cards) {
            if (card.getCardId().equalsIgnoreCase(cardId))
                return true;
        }
        return false;
    }

    private ArrayList<ServerCard> getCardsWithName(String cardName, List<ServerCard> cards) {
        ArrayList<ServerCard> cards1 = new ArrayList<>();
        if (cardName == null || cards == null)
            return cards1;
        for (ServerCard card : cards) {
            if (card.getName().equalsIgnoreCase(cardName))
                cards1.add(card);
        }
        return cards1;
    }

    public ServerCard getCard(String cardId) {
        if (hasCard(cardId, heroes))
            return getCard(cardId, heroes);
        if (hasCard(cardId, minions))
            return getCard(cardId, minions);
        if (hasCard(cardId, spells))
            return getCard(cardId, spells);
        return null;
    }

    private ServerCard getCard(String cardId, List<ServerCard> cards) {
        for (ServerCard card : cards) {
            if (card.getCardId().equalsIgnoreCase(cardId))
                return card;
        }
        return null;
    }

    void addCard(String cardName, Collection originalCards, String username) {//for account collections
        ServerCard card = DataCenter.getCard(cardName, originalCards);
        assert card != null : "Invalid card name given to addCard.";
        int number = 1;
        String cardId = (username + "_" + cardName + "_").replaceAll(" ", "");
        while (hasCard(cardId + number))
            number++;
        ServerCard newCard = new ServerCard(card, username, number);
        addCard(newCard);
    }

    public void addCard(ServerCard card) {//for shop
        assert card != null : "addCard card is null.";
        if (hasCard(card.getCardId())) {
            GameServer.serverPrint("Error: Account does not own '" + card.getCardId() + "'");
            return;
        }
        switch (card.getType()) {
            case HERO:
                heroes.add(card);
                break;
            case MINION:
                minions.add(card);
                break;
            case SPELL:
                spells.add(card);
                break;
        }
    }

    public Deck extractDeck(ExportedDeck exportedDeck) throws LogicException {
        Deck deck = new Deck(exportedDeck.getName());
        ArrayList<ServerCard> hero = getCardsWithName(exportedDeck.getHeroName(), heroes);
        if (hero.isEmpty())
            throw new ClientException("you have not the hero");
        deck.addCard(hero.get(0));
        for (Map.Entry<String, Integer> entry :
                exportedDeck.getCards().entrySet()) {
            ArrayList<ServerCard> cards = getCardsWithName(entry.getKey(), minions);
            cards.addAll(getCardsWithName(entry.getKey(), spells));
            if (cards.size() < entry.getValue())
                throw new ClientException("you have not enough cards (buy " + entry.getKey() + " from shop");
            for (int i = 0; i < entry.getValue(); i++) {
                deck.addCard(cards.get(i));
            }
        }
        return deck;
    }

    public void clearCollection() {
        heroes.clear();
        minions.clear();
        spells.clear();
    }

    public int Size(){
        return heroes.size() + minions.size() + spells.size();
    }

    @Override
    public boolean equals(Object object) {
        if(object == null){
            return false;
        }

        if( object.getClass() != this.getClass() ){
             return false;
        }

        if(this == object){
            return true;
        }

        Collection other = (Collection) object;

        if (heroes.size() != other.heroes.size() ||
            minions.size() != other.minions.size() ||
            spells.size() != other.spells.size()){
                    return false;
                }  

        if(!this.heroes.equals(other.heroes)){
            return false;
        }

        if(!this.minions.equals(other.minions)){
            return false;
        }

        if(!this.spells.equals(other.spells)){
            return false;
        }

        return true;
    }

    @Override
    public int hashCode(){
        int result = 1;
        int primeNumberForHash = 31;
        
        result = primeNumberForHash * result + (heroes != null ? heroes.hashCode() : 0);
        result = primeNumberForHash * result + (minions != null ? minions.hashCode() : 0);
        result = primeNumberForHash * result + (spells != null? spells.hashCode() : 0);
        return result;
    }
}