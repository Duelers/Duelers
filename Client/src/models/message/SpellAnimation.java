package models.message;

import shared.models.card.spell.AvailabilityType;
import shared.models.game.map.Cell;

import java.util.Set;

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
