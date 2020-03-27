package org.projectcardboard.client.models.message;

import java.util.Set;

import shared.models.card.spell.AvailabilityType;
import shared.models.game.map.Cell;

public class SpellAnimation {
    private Set<Cell> cells;
    private String fxName;

    public Set<Cell> getCells() {
        return cells;
    }

    public String getFxName() {
        return fxName;
    }
}
