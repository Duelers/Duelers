package server.gameCenter.models.game.availableActions;

import server.gameCenter.models.game.ServerTroop;
import shared.models.game.map.Cell;

import java.util.Collections;
import java.util.List;

public class Move {
    private final ServerTroop troop;
    private final List<Cell> targets;

    Move(ServerTroop troop, List<Cell> targets) {
        this.troop = troop;
        this.targets = targets;
    }

    public ServerTroop getTroop() {
        return troop;
    }

    public List<Cell> getTargets() {
        return Collections.unmodifiableList(targets);
    }
}
