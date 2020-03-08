package server.dataCenter.models.card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TempDeck {
    private final String deckName;
    private String heroId;
    private String itemId;
    private final List<String> othersIds = new ArrayList<>();

    public TempDeck(Deck deck) {
        this.deckName = deck.getDeckName();
        if (deck.getHero() != null) {
            this.heroId = deck.getHero().getCardId();
        }

        for (ServerCard card : deck.getOthers()) {
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
