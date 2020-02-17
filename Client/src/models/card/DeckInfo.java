package models.card;

import models.game.GameType;

public class DeckInfo {
    private String deckName;
    private String HeroName;
    private GameType type;

    public String getDeckName() {
        return deckName;
    }

    public String getHeroName() {
        return HeroName;
    }

    public GameType getType() {
        return type;
    }
}
