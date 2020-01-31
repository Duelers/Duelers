package server.gameCenter.models.game.availableActions;

import server.gameCenter.models.game.Troop;
import server.gameCenter.models.map.Position;

import java.util.Collections;
import java.util.List;

public class Move {
    private Troop troop;
    private List<Position> targets;

    Move(Troop troop, List<Position> targets) {
        this.troop = troop;
        this.targets = targets;
    }

    public Troop getTroop() {
        return troop;
    }

    public List<Position> getTargets() {
        return Collections.unmodifiableList(targets);
    }
}
