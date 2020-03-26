package server.clientPortal.models.message;

import shared.models.card.spell.AvailabilityType;
import shared.models.game.map.Cell;

import java.util.Set;

class SpellAnimation {
    private final Set<Cell> cells;
    private final String fxName;

    SpellAnimation(Set<Cell> cells, String fxName) {
        this.cells = cells;
        this.fxName = fxName;
    }
}
