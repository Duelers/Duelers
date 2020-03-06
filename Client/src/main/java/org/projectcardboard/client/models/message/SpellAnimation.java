package org.projectcardboard.client.models.message;

import java.util.Set;

import shared.models.card.spell.AvailabilityType;
import shared.models.game.map.Cell;

public class SpellAnimation {
    private Set<Cell> cells;
    private AvailabilityType availabilityType;

    public Set<Cell> getCells() {
        return cells;
    }

    public AvailabilityType getAvailabilityType() {
        return availabilityType;
    }
}
