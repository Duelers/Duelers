package org.projectcardboard.client.models.card;

import shared.models.game.GameType;

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
