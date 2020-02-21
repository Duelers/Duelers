package server.dataCenter.models.account;

import server.Server;
import server.dataCenter.DataCenter;
import server.dataCenter.models.card.Card;
import server.dataCenter.models.card.Deck;
import server.dataCenter.models.card.ExportedDeck;
import server.exceptions.ClientException;
import server.exceptions.LogicException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Collection {
    private List<Card> heroes = new ArrayList<>();
    private List<Card> minions = new ArrayList<>();
    private List<Card> spells = new ArrayList<>();

    boolean hasCard(String cardId) {
        return hasCard(cardId, heroes) || hasCard(cardId, minions) || hasCard(cardId, spells);
    }

    private boolean hasCard(String cardId, List<Card> cards) {
        if (cardId == null || cards == null)
            return false;
        for (Card card : cards) {
            if (card.getCardId().equalsIgnoreCase(cardId))
                return true;
        }
        return false;
    }

    private ArrayList<Card> getCardsWithName(String cardName, List<Card> cards) {
        ArrayList<Card> cards1 = new ArrayList<>();
        if (cardName == null || cards == null)
            return cards1;
        for (Card card : cards) {
            if (card.getName().equalsIgnoreCase(cardName))
                cards1.add(card);
        }
        return cards1;
    }

    public Card getCard(String cardId) {
        if (hasCard(cardId, heroes))
            return getCard(cardId, heroes);
        if (hasCard(cardId, minions))
            return getCard(cardId, minions);
        if (hasCard(cardId, spells))
            return getCard(cardId, spells);
        return null;
    }

    private Card getCard(String cardId, List<Card> cards) {
        for (Card card : cards) {
            if (card.getCardId().equalsIgnoreCase(cardId))
                return card;
        }
        return null;
    }

    void addCard(String cardName, Collection originalCards, String username) throws ClientException {//for account collections
        Card card = DataCenter.getCard(cardName, originalCards);
        if (card == null) {
            Server.getInstance().serverPrint("Invalid CardName!");
            return;
        }
        int number = 1;
        String cardId = (username + "_" + cardName + "_").replaceAll(" ", "");
        while (hasCard(cardId + number))
            number++;
        Card newCard = new Card(card, username, number);
        //removed to allow the user to have all items in their collection
        //if (newCard.getType() == CardType.USABLE_ITEM && items.size() >= 3) {
        //    throw new ClientException("you can't have more than 3 items");
        //}
        addCard(newCard);
    }

    public void addCard(Card card) {//for shop
        if (card == null) {
            Server.getInstance().serverPrint("Error: Card is null");
            return;
        }
        if (hasCard(card.getCardId())) {
            Server.getInstance().serverPrint("Error: Account does not own '" + card.getCardId() + "'");
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

    public void removeCard(Card card) {
        heroes.remove(card);
        minions.remove(card);
        spells.remove(card);
    }

    public Deck extractDeck(ExportedDeck exportedDeck) throws LogicException {
        Deck deck = new Deck(exportedDeck.getName());
        ArrayList<Card> hero = getCardsWithName(exportedDeck.getHeroName(), heroes);
        if (hero.isEmpty())
            throw new ClientException("you have not the hero");
        deck.addCard(hero.get(0));
        for (Map.Entry<String, Integer> entry :
                exportedDeck.getOtherCards().entrySet()) {
            ArrayList<Card> cards = getCardsWithName(entry.getKey(), minions);
            cards.addAll(getCardsWithName(entry.getKey(), spells));
            if (cards.size() < entry.getValue())
                throw new ClientException("you have not enough cards (buy " + entry.getKey() + " from shop");
            for (int i = 0; i < entry.getValue(); i++) {
                deck.addCard(cards.get(i));
            }
        }
        return deck;
    }

    public List<Card> getHeroes() {
        return Collections.unmodifiableList(heroes);
    }

    public List<Card> getMinions() {
        return Collections.unmodifiableList(minions);
    }

    public List<Card> getSpells() {
        return Collections.unmodifiableList(spells);
    }

    public Card findHero(String heroId) {
        return findCardInList(heroId, heroes);
    }

    private Card findCardInList(String cardId, List<Card> list) {
        for (Card card : list) {
            if (card.getCardId().equalsIgnoreCase(cardId)) return card;
        }
        return null;
    }

    public Card findOthers(String cardId) {
        Card card = findCardInList(cardId, minions);
        if (card != null) return card;
        return findCardInList(cardId, spells);
    }

    public int count(String cardName) {
        return find(cardName).size();
    }

    private List<Card> find(String cardName) {
        List<Card> result = new ArrayList<>();
        findInList(heroes, result, cardName);
        findInList(minions, result, cardName);
        findInList(spells, result, cardName);
        return result;
    }

    private void findInList(List<Card> list, List<Card> result, String cardName) {
        for (Card card : list) {
            if (card.isSameAs(cardName)) {
                result.add(card);
            }
        }
    }

    public Collection toShowing() {
        Collection collection = new Collection();
        convertListToShowing(collection.heroes, heroes);
        convertListToShowing(collection.spells, spells);
        convertListToShowing(collection.minions, minions);
        return collection;
    }

    public Collection searchCollection(String cardName) {
        Collection result = new Collection();
        searchInList(heroes, result.heroes, cardName);
        searchInList(minions, result.minions, cardName);
        searchInList(spells, result.spells, cardName);
        return result;
    }

    private void searchInList(List<Card> list, List<Card> results, String cardName) {
        for (Card card : list) {
            if (card.nameContains(cardName)) {
                results.add(card);
            }
        }
    }

    private void convertListToShowing(List<Card> newList, List<Card> mainList) {
        Outer:
        for (Card hero : mainList) {
            for (Card other : newList) {
                if (hero.isSameAs(other.getName())) continue Outer;
            }
            newList.add(hero);
        }
    }

    public String canAddCardTo(String cardName, Deck deck) {
        for (Card hero : heroes) {
            if (hero.isSameAs(cardName) && !deck.hasHero(hero)) {
                return hero.getCardId();
            }
        }
        for (Card minion : minions) {
            if (minion.isSameAs(cardName) && !deck.hasCard(minion.getName())) {
                return minion.getCardId();
            }
        }
        for (Card spell : spells) {
            if (spell.isSameAs(cardName) && !deck.hasCard(spell.getName())) {
                return spell.getCardId();
            }
        }
        return null;
    }
}