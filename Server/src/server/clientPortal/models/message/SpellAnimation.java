package server.clientPortal.models.message;

import server.dataCenter.models.card.spell.AvailabilityType;
import shared.models.game.map.Cell;

import java.util.Set;

class SpellAnimation {
    private final Set<Cell> cells;
    private final AvailabilityType availabilityType;

    SpellAnimation(Set<Cell> cells, AvailabilityType availabilityType) {
        this.cells = cells;
        this.availabilityType = availabilityType;
    }
}
