package shared.models.card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BaseDeck<CardType extends Card> {

    private final int MIN_DECK_SIZE = 5;
    private final int MAX_DECK_SIZE = 40;

    protected String deckName;
    protected CardType hero;
    protected ArrayList<CardType> cards = new ArrayList<>();

    public BaseDeck(String deckName) {
        this.deckName = deckName;
    }

    public BaseDeck(String deckName, CardType hero, ArrayList<CardType> cards) {
        this.deckName = deckName;
        this.hero = hero;
        for (CardType card : cards) {
            //noinspection unchecked //I belive this is redundant as CardType extends Card.
            this.cards.add((CardType) new Card(card));
        }
    }

    public String getName() {
        return deckName;
    }

    public CardType getHero() {
        return hero;
    }

    public List<CardType> getCards() {
        return Collections.unmodifiableList(cards);
    }

    public CardType getCard(String cardName) {
        if (this.hero != null && hero.isSameAs(cardName)) return hero;

        for (CardType card : cards) {
            if (card.isSameAs(cardName)) return card;
        }
        return null;
    }

    public boolean isValid() {
        if (hero == null) return false;
        return this.cards.size() >= MIN_DECK_SIZE && this.cards.size() <= MAX_DECK_SIZE;
    }

    public boolean hasHero(Card hero) {
        if (this.hero == null) return false;
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

