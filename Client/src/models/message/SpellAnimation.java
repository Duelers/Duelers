package models.message;

import server.dataCenter.models.card.spell.AvailabilityType;
import server.gameCenter.models.map.Cell;
// import models.game.map.Position;

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
