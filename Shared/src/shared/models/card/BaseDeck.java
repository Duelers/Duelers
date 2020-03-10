package shared.models.card;

import java.util.ArrayList;

public class BaseDeck {
    protected String deckName;
    protected Card hero;
    protected ArrayList<Card> cards = new ArrayList<>();

    public BaseDeck(String deckName){
        this.deckName = deckName;
    }

    public BaseDeck(String deckName, Card hero, ArrayList<? extends Card> cards) {
        this.deckName = deckName;
        this.hero = hero;
        for (Card card : cards) {
            this.cards.add(new Card(card));
        }
    }

}

