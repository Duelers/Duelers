package models.card;

import shared.models.card.Card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TempDeck {
    private final String deckName;
    private String heroId;
    private String itemId;
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

    public String getItemId() {
        return itemId;
    }

    public List<String> getOthersIds() {
        return Collections.unmodifiableList(othersIds);
    }
}
