package server.clientPortal.models.message;

import server.dataCenter.models.card.spell.AvailabilityType;
import server.gameCenter.models.map.Cell;

import java.util.Set;

class SpellAnimation {
    private final Set<Cell> cells;
    private final AvailabilityType availabilityType;

    SpellAnimation(Set<Cell> cells, AvailabilityType availabilityType) {
        this.cells = cells;
        this.availabilityType = availabilityType;
    }
}
