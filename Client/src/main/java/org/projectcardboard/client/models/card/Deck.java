package org.projectcardboard.client.models.card;


import org.projectcardboard.client.controller.Client;
import shared.models.card.Card;
import shared.models.card.ICard;
import org.projectcardboard.client.models.account.Collection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private final String deckName;
    private final Card hero;
    private final ArrayList<Card> cards = new ArrayList<>();

    public Deck(TempDeck tempDeck, Collection collection) {
        this.deckName = tempDeck.getDeckName();
        this.hero = collection.findHero(tempDeck.getHeroId());
        for (String cardId : tempDeck.getCardIds()) {
            cards.add(collection.findOthers(cardId));
        }
    }

    // TODO: overriding equals without changing hashcode is normally no bueno.
    // there's probably a safer way for us to accomplish this.
    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj instanceof Deck) {
            Deck deck = (Deck) obj;
            if (!deckName.equals(deck.getName())) return false;
            if (hero == null ^ deck.hero == null) return false;
            if (hero != null && !hero.equals(deck.hero)) return false;

            if (cards.size() != deck.cards.size()) return false;
            for (Card cardInThis : cards) {
                if (!deck.cards.contains(cardInThis)) return false;
            }
            return true;
        }
        if (obj instanceof TempDeck) {
            TempDeck deck = (TempDeck) obj;
            if (!deckName.equals(deck.getDeckName())) return false;
            if (hero == null ^ deck.getHeroId() == null) return false;
            if (hero != null && !hero.getCardId().equalsIgnoreCase(deck.getHeroId())) return false;

            if (cards.size() != deck.getCardIds().size()) return false;
            for (Card cardInThis : cards) {
                if (!deck.getCardIds().contains(cardInThis.getCardId())) return false;
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

    public List<Card> getCards() {
        return Collections.unmodifiableList(this.cards);
    }

    public boolean areSame(String deckName) {
        return this.deckName.equalsIgnoreCase(deckName);
    }

    public boolean isValid() {
        if (hero == null) return false;
        return cards.size() == 20;
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
                for (Card cardInThis : cards) {
                    if (cardInThis.isSameAs(card.getName())) count++;
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
        for (Card card : cards) {
            if (card.equals(other)) {
                return true;
            }
        }
        return false;
    }

    public Card getCard(String cardName) {
        if (hero != null && hero.isSameAs(cardName)) return hero;

        for (Card card : cards) {
            if (card.isSameAs(cardName)) return card;
        }
        return null;
    }
}