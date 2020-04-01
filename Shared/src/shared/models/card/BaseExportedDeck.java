package shared.models.card;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class BaseExportedDeck<CardType extends Card, DeckType extends BaseDeck<CardType>> {
  private final String name;
  private String heroName;
  private final HashMap<String, Integer> cards = new HashMap<>();

  public BaseExportedDeck(DeckType deck) {
    name = deck.getName();
    if (deck.getHero() != null) {
      heroName = deck.getHero().getName();
    }
    for (Card card : deck.getCards()) {
      cards.merge(card.getName(), 1, Integer::sum);
    }
  }

  public String getName() {
    return name;
  }

  public String getHeroName() {
    return heroName;
  }

  /**
   * @return all cards except the hero.
   */
  public Map<String, Integer> getCards() {
    return Collections.unmodifiableMap(cards);
  }
}
