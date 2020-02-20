package localDataCenter.models.card.spell;

import localDataCenter.models.card.Deck;
import localGameCenter.models.game.GameType;

public class DeckInfo {
    private String deckName;
    private String HeroName;
    private GameType type;


    public DeckInfo(Deck deck) {
        this.deckName = deck.getDeckName();
        this.HeroName = deck.getHero().getName();
    }
}
