package org.projectcardboard.client.models.card;


import org.projectcardboard.client.controller.Client;
import shared.models.card.BaseDeck;
import shared.models.card.Card;
import shared.models.card.ICard;
import org.projectcardboard.client.models.account.Collection;

import java.util.ArrayList;

public class Deck extends BaseDeck<Card> {

    public Deck(TempDeck tempDeck, Collection collection) {
        super(tempDeck.getDeckName());
        this.hero = collection.findHero(tempDeck.getHeroId());
        for (String cardId : tempDeck.getCardIds()) {
            this.cards.add(collection.findOthers(cardId));
        }
    }

    // TODO: overriding equals without changing hashcode is normally no bueno.
    // there's probably a safer way for us to accomplish this.
    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj instanceof Deck) {
            Deck deck = (Deck) obj;
            if (!deckName.equals(deck.getName()))
                return false;
            if (hero == null ^ deck.hero == null)
                return false;
            if (hero != null && !hero.equals(deck.hero))
                return false;

            if (cards.size() != deck.cards.size())
                return false;
            for (Card cardInThis : cards) {
                if (!deck.cards.contains(cardInThis))
                    return false;
            }
            return true;
        }
        if (obj instanceof TempDeck) {
            TempDeck deck = (TempDeck) obj;
            if (!deckName.equals(deck.getDeckName()))
                return false;
            if (hero == null ^ deck.getHeroId() == null)
                return false;
            if (hero != null && !hero.getCardId().equalsIgnoreCase(deck.getHeroId()))
                return false;

            if (cards.size() != deck.getCardIds().size())
                return false;
            for (Card cardInThis : cards) {
                if (!deck.getCardIds().contains(cardInThis.getCardId()))
                    return false;
            }
            return true;
        }
        return false;
    }

    public boolean isMain() {
        return Client.getInstance().getAccount().isMainDeck(this);
    }

    public int count(ICard card) {
        switch (card.getType()) {
            case HERO:
                if (hero != null && hero.isSameAs(card.getName()))
                    return 1;
                return 0;
            case MINION:
            case SPELL:
                int count = 0;
                for (Card cardInThis : cards) {
                    if (cardInThis.isSameAs(card.getName()))
                        count++;
                }
                return count;
            default:
                return 0;
        }
    }
}
