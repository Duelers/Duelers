package models.card;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ExportedDeck {
    private String name;
    private String heroName;
    private HashMap<String, Integer> otherCards = new HashMap<>();

    public ExportedDeck(Deck deck) {
        name = deck.getName();
        if (deck.getHero() != null) {
            heroName = deck.getHero().getName();
        }
        for (Card other : deck.getOthers()) {
            otherCards.merge(other.getName(), 1, Integer::sum);
        }
    }

    public String getName() {
        return name;
    }

    public String getHeroName() {
        return heroName;
    }

    public Map<String, Integer> getOtherCards() {
        return Collections.unmodifiableMap(otherCards);
    }
}
