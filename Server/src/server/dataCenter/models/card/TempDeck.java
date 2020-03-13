package server.dataCenter.models.card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TempDeck {
    private final String deckName;
    private String heroId;
    private final List<String> cardIds = new ArrayList<>();

    public TempDeck(Deck deck) {
        this.deckName = deck.getName();
        if (deck.getHero() != null) {
            this.heroId = deck.getHero().getCardId();
        }

        for (ServerCard card : deck.getCards()) {
            this.cardIds.add(card.getCardId());
        }
    }

    public String getDeckName() {
        return deckName;
    }

    String getHeroId() {
        return heroId;
    }

    List<String> getCardIds() {
        return Collections.unmodifiableList(cardIds);
    }
}
