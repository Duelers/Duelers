package shared.models.card;

import java.util.*;

public class BaseDeck<CardType extends Card> {

  private final int MIN_DECK_SIZE = 5;
  private final int MAX_DECK_SIZE = 40;

  protected String deckName;
  protected CardType hero;
  protected ArrayList<CardType> cards = new ArrayList<>();
  protected Map<String, Integer> factions = new HashMap<>();

  public BaseDeck(String deckName) {
    this.deckName = deckName;
  }

  public BaseDeck(String deckName, CardType hero, ArrayList<CardType> cards) {
    this.deckName = deckName;
    this.hero = hero;
    for (CardType card : cards) {
      // noinspection unchecked //I belive this is redundant as CardType extends Card.
      Card newC = new Card(card);
      this.cards.add((CardType) newC);
      this.factions.merge(newC.getFaction(), 1, Integer::sum);
    }
    System.out.println("=!!=" + Collections.singletonList(this.factions));   // Does not seem to update
  }


  public String getName() {
    return deckName;
  }

  public CardType getHero() {
    return hero;
  }

  public Map<String, Integer> getFactionList() {
    System.out.println("0000" + Collections.singletonList(this.factions));
    return this.factions;
  }

  public List<CardType> getCards() {
    return Collections.unmodifiableList(cards);
  }

  public CardType getCard(String cardName) {
    if (this.hero != null && hero.isSameAs(cardName))
      return hero;

    for (CardType card : cards) {
      if (card.isSameAs(cardName))
        return card;
    }
    return null;
  }

  public boolean isValid() {
    if (hero == null)
      return false;
    return this.cards.size() >= MIN_DECK_SIZE && this.cards.size() <= MAX_DECK_SIZE;
  }

  public boolean hasHero(Card hero) {
    if (this.hero == null)
      return false;
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

}

