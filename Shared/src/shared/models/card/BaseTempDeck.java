package shared.models.card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BaseTempDeck<CardType extends Card, DeckType extends BaseDeck<CardType>> {

  private final String deckName;
  private String heroId;
  private final ArrayList<String> cardIds = new ArrayList<>();

  public BaseTempDeck(DeckType deck) {
    this.deckName = deck.getName();
    if (deck.getHero() != null) {
      this.heroId = deck.getHero().getCardId();
    }
    for (Card card : deck.getCards()) {
      this.cardIds.add(card.getCardId());
    }
  }

  public String getDeckName() {
    return deckName;
  }

  public String getHeroId() {
    return heroId;
  }

  public List<String> getCardIds() {
    return Collections.unmodifiableList(cardIds);
  }

}
