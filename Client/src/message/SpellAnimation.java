package message;

import localDataCenter.models.card.spell.AvailabilityType;
import localGameCenter.models.map.Position;

import java.util.Set;

public class SpellAnimation {
    private final Set<Position> positions;
    private final AvailabilityType availabilityType;

    SpellAnimation(Set<Position> positions, AvailabilityType availabilityType) {
        this.positions = positions;
        this.availabilityType = availabilityType;
    }

    public Set<Position> getPositions() {
        return positions;
    }

    public AvailabilityType getAvailabilityType() {
        return availabilityType;
    }
}
