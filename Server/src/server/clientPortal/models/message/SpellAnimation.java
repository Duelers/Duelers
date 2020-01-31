package server.clientPortal.models.message;

import server.dataCenter.models.card.spell.AvailabilityType;
import server.gameCenter.models.map.Position;

import java.util.Set;

class SpellAnimation {
    private final Set<Position> positions;
    private final AvailabilityType availabilityType;

    SpellAnimation(Set<Position> positions, AvailabilityType availabilityType) {
        this.positions = positions;
        this.availabilityType = availabilityType;
    }
}
