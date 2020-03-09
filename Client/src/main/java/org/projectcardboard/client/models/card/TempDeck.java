package org.projectcardboard.client.models.card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import shared.models.card.Card;

public class TempDeck {
    private final String deckName;
    private String heroId;
    private final ArrayList<String> othersIds = new ArrayList<>();

    public TempDeck(Deck deck) {
        this.deckName = deck.getName();
        if (deck.getHero() != null) {
            this.heroId = deck.getHero().getCardId();
        }
        for (Card card : deck.getOthers()) {
            this.othersIds.add(card.getCardId());
        }
    }

    public String getDeckName() {
        return deckName;
    }

    public String getHeroId() {
        return heroId;
    }

    public List<String> getOthersIds() {
        return Collections.unmodifiableList(othersIds);
    }
}
