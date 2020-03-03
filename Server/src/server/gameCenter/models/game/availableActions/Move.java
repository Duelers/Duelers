package server.gameCenter.models.game.availableActions;

import shared.models.game.ServerTroop;
import shared.models.game.map.Cell;

import java.util.Collections;
import java.util.List;

public class Move {
    private ServerTroop troop;
    private List<Cell> targets;

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
