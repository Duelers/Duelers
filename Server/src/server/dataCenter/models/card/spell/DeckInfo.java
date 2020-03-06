package server.dataCenter.models.card.spell;

import server.dataCenter.models.card.Deck;
import shared.models.game.GameType;

public class DeckInfo {
    private GameType type;


    public DeckInfo(Deck deck) {
        String deckName = deck.getDeckName();
        String heroName = deck.getHero().getName();
    }
}
