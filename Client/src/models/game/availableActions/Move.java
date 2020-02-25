package models.game.availableActions;

import server.clientPortal.models.comperessedData.CompressedTroop;
import server.gameCenter.models.map.Cell;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Move {
    private CompressedTroop troop;
    private List<Cell> targets;

    Move(CompressedTroop troop, ArrayList<Cell> targets) {
        this.troop = troop;
        this.targets = targets;
    }

    public CompressedTroop getTroop() {
        return troop;
    }

    List<Cell> getTargets() {
        return Collections.unmodifiableList(targets);
    }
}
