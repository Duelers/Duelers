package server.dataCenter.models.card.spell;

import server.dataCenter.models.card.Deck;
import server.gameCenter.models.game.GameType;

public class DeckInfo {
    private String deckName;
    private String HeroName;
    private GameType type;


    public DeckInfo(Deck deck) {
        this.deckName = deck.getDeckName();
        this.HeroName = deck.getHero().getName();
    }
}
