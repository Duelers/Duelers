package server.gameCenter.models.game.availableActions;

import server.gameCenter.models.game.Troop;
import server.gameCenter.models.map.Cell;

import java.util.Collections;
import java.util.List;

public class Move {
    private Troop troop;
    private List<Cell> targets;

    Move(Troop troop, List<Cell> targets) {
        this.troop = troop;
        this.targets = targets;
    }

    public Troop getTroop() {
        return troop;
    }

    public List<Cell> getTargets() {
        return Collections.unmodifiableList(targets);
    }
}
