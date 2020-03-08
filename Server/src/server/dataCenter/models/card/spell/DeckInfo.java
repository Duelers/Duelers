package server.dataCenter.models.card.spell;

import server.dataCenter.models.card.Deck;
import shared.models.game.GameType;

public class DeckInfo {
    private final String deckName;
    private final String HeroName;
    private GameType type;


    public DeckInfo(Deck deck) {
        this.deckName = deck.getDeckName();
        this.HeroName = deck.getHero().getName();
    }
}
