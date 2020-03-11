package shared.models.card;

import java.util.ArrayList;

public class BaseDeck<CardType extends Card> {
    protected String deckName;
    protected CardType hero;
    protected ArrayList<CardType> cards = new ArrayList<>();

    public BaseDeck(String deckName){
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

    public String getDeckName() {
        return deckName;
    }



}

