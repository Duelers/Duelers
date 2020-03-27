package org.projectcardboard.client.models.account;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.projectcardboard.client.models.card.Deck;

import shared.models.account.BaseCollection;
import shared.models.card.Card;

public class Collection extends BaseCollection<Card> {
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

  private List<Card> find(String cardName) {
    List<Card> result = new ArrayList<>();
    findInList(heroes, result, cardName);
    findInList(minions, result, cardName);
    findInList(spells, result, cardName);
    return result;
  }

  private void searchInList(List<Card> list, List<Card> results, String cardName) {
    for (Card card : list) {
      if (card.nameContains(cardName)) {
        results.add(card);
      }
    }
  }

  public Card findHero(String heroId) {
    return findCardInList(heroId, heroes);
  }

  public Card findOthers(String cardId) {
    Card card = findCardInList(cardId, minions);
    if (card != null)
      return card;
    return findCardInList(cardId, spells);
  }

  private Card findCardInList(String cardId, List<Card> list) {
    for (Card card : list) {
      if (card.getCardId().equalsIgnoreCase(cardId))
        return card;
    }
    return null;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Collection))
      return false;
    Collection other = (Collection) o;

    if (heroes.size() != other.heroes.size() || minions.size() != other.minions.size()
        || spells.size() != other.spells.size())
      return false;

    for (Card card : heroes) {
      if (!other.heroes.contains(card))
        return false;
    }

    for (Card card : minions) {
      if (!other.minions.contains(card))
        return false;
    }

    for (Card card : spells) {
      if (!other.spells.contains(card))
        return false;
    }

    return true;
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
    Outer: for (Card hero : mainList) {
      for (Card other : newList) {
        if (hero.isSameAs(other.getName()))
          continue Outer;
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
}
