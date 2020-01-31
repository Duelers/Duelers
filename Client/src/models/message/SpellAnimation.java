package models.message;

import models.card.spell.AvailabilityType;
import models.game.map.Position;

import java.util.Set;

public class SpellAnimation {
    private Set<Position> positions;
    private AvailabilityType availabilityType;

    public Set<Position> getPositions() {
        return positions;
    }

    public AvailabilityType getAvailabilityType() {
        return availabilityType;
    }
}
