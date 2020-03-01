package server.dataCenter.models.card;

import shared.models.card.Card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TempDeck {
    private String deckName;
    private String heroId;
    private String itemId;
    private List<String> othersIds = new ArrayList<>();

    public TempDeck(Deck deck) {
        this.deckName = deck.getDeckName();
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

    String getHeroId() {
        return heroId;
    }

    String getItemId() {
        return itemId;
    }

    List<String> getOthersIds() {
        return Collections.unmodifiableList(othersIds);
    }
}
