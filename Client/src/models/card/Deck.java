package models.card;


import controller.Client;
import shared.models.card.Card;
import shared.models.card.ICard;
import models.account.Collection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private String deckName;
    private Card hero;
    private ArrayList<Card> others = new ArrayList<>();

    public Deck(TempDeck tempDeck, Collection collection) {
        this.deckName = tempDeck.getDeckName();
        this.hero = collection.findHero(tempDeck.getHeroId());
        for (String cardId : tempDeck.getOthersIds()) {
            others.add(collection.findOthers(cardId));
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj instanceof Deck) {
            Deck deck = (Deck) obj;
            if (!deckName.equals(deck.getName())) return false;
            if (hero == null ^ deck.hero == null) return false;
            if (hero != null && !hero.equals(deck.hero)) return false;

            if (others.size() != deck.others.size()) return false;
            for (Card other : others) {
                if (!deck.others.contains(other)) return false;
            }
            return true;
        }
        if (obj instanceof TempDeck) {
            TempDeck deck = (TempDeck) obj;
            if (!deckName.equals(deck.getDeckName())) return false;
            if (hero == null ^ deck.getHeroId() == null) return false;
            if (hero != null && !hero.getCardId().equalsIgnoreCase(deck.getHeroId())) return false;

            if (others.size() != deck.getOthersIds().size()) return false;
            for (Card other : others) {
                if (!deck.getOthersIds().contains(other.getCardId())) return false;
            }
            return true;
        }
        return false;
    }

    public String getName() {
        return this.deckName;
    }

    public Card getHero() {
        return this.hero;
    }

    public List<Card> getOthers() {
        return Collections.unmodifiableList(this.others);
    }

    public boolean areSame(String deckName) {
        return this.deckName.equalsIgnoreCase(deckName);
    }

    public boolean isValid() {
        if (hero == null) return false;
        return others.size() == 20;
    }

    public boolean isMain() {
        return Client.getInstance().getAccount().isMainDeck(this);
    }

    public int count(ICard card) {
        switch (card.getType()) {
            case HERO:
                if (hero != null && hero.isSameAs(card.getName())) return 1;
                return 0;
            case MINION:
            case SPELL:
                int count = 0;
                for (Card other : others) {
                    if (other.isSameAs(card.getName())) count++;
                }
                return count;
            default:
                return 0;
        }
    }

    public boolean hasHero(Card hero) {
        if (this.hero == null) return false;
        return this.hero.equals(hero);
    }

    public boolean hasCard(Card other) {
        for (Card card : others) {
            if (card.equals(other)) {
                return true;
            }
        }
        return false;
    }

    public Card getCard(String cardName) {
        if (hero != null && hero.isSameAs(cardName)) return hero;

        for (Card other : others) {
            if (other.isSameAs(cardName)) return other;
        }
        return null;
    }
}