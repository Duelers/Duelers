package models.account;

import server.dataCenter.models.card.Card;
import server.dataCenter.models.card.Deck;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Collection {
    private List<Card> heroes = new ArrayList<>();
    private List<Card> minions = new ArrayList<>();
    private List<Card> spells = new ArrayList<>();

    // When two cards cost the same mana, sort alphabetically by name.
    // Note that the current implementation only sorts by first character.
    // Thus Az could appear before Ab, but Cz is always before Da.
    private Comparator<Card> compareCostThenName = Comparator.comparingInt(Card::getManaCost).thenComparingInt(c -> c.getCardId().charAt(0));

    public List<Card> getHeroes() {
        heroes.sort(compareCostThenName);
        return heroes;
    }

    public List<Card> getMinions() {
        minions.sort(compareCostThenName);
        return minions;
    }

    public List<Card> getSpells() {
        spells.sort(compareCostThenName);
        return spells;
    }

    public Collection searchCollection(String cardName) {
        Collection result = new Collection();
        searchInList(heroes, result.heroes, cardName);
        searchInList(minions, result.minions, cardName);
        searchInList(spells, result.spells, cardName);
        return result;
    }

    public Card getCard(String cardName) {
        List<Card> result = find(cardName);
        if (result.size() > 0) {
            return result.get(0);
        }
        return null;
    }

    private void searchInList(List<Card> list, List<Card> results, String cardName) {
        for (Card card : list) {
            if (card.nameContains(cardName)) {
                results.add(card);
            }
        }
    }

    public void addCard(Card card) {//for shop
        if (card == null) {
            return;
        }
        if (hasCard(card.getCardId())) {
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

    private boolean hasCard(String cardId) {
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

    public Card findHero(String heroId) {
        return findCardInList(heroId, heroes);
    }

    public Card findOthers(String cardId) {
        Card card = findCardInList(cardId, minions);
        if (card != null) return card;
        return findCardInList(cardId, spells);
    }

    private Card findCardInList(String cardId, List<Card> list) {
        for (Card card : list) {
            if (card.getCardId().equalsIgnoreCase(cardId)) return card;
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Collection)) return false;
        Collection other = (Collection) o;

        if (heroes.size() != other.heroes.size() ||
                minions.size() != other.minions.size() ||
                spells.size() != other.spells.size())
            return false;

        for (Card card : heroes) {
            if (!other.heroes.contains(card)) return false;
        }

        for (Card card : minions) {
            if (!other.minions.contains(card)) return false;
        }

        for (Card card : spells) {
            if (!other.spells.contains(card)) return false;
        }

        return true;
    }

    public Card findLast(String cardName) {
        List<Card> result = find(cardName);
        if (result.size() == 0) return null;
        return result.get(result.size() - 1);
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

    public int count(String cardName) {
        return find(cardName).size();
    }

    public Collection toShowing() {
        Collection collection = new Collection();
        convertListToShowing(collection.heroes, heroes);
        convertListToShowing(collection.spells, spells);
        convertListToShowing(collection.minions, minions);
        return collection;
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
            if (minion.isSameAs(cardName) && !deck.hasCard(minion)) {
                return minion.getCardId();
            }
        }
        for (Card spell : spells) {
            if (spell.isSameAs(cardName) && !deck.hasCard(spell)) {
                return spell.getCardId();
            }
        }
        return null;
    }

    public Card removeCard(String cardName) {
        Card card;
        if (null != (card = removeFromList(heroes, cardName))) return card;
        if (null != (card = removeFromList(minions, cardName))) return card;
        if (null != (card = removeFromList(spells, cardName))) return card;
        return null;
    }

    private Card removeFromList(List<Card> list, String cardName) {
        List<Card> result = new ArrayList<>();
        findInList(list, result, cardName);
        if (result.size() > 0) {
            list.remove(result.get(0));
            return result.get(0);
        }
        return null;
    }
}