package models.game.availableActions;

import models.comperessedData.CompressedTroop;
import models.game.map.Position;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Move {
    private CompressedTroop troop;
    private List<Position> targets;

    Move(CompressedTroop troop, ArrayList<Position> targets) {
        this.troop = troop;
        this.targets = targets;
    }

    public CompressedTroop getTroop() {
        return troop;
    }

    List<Position> getTargets() {
        return Collections.unmodifiableList(targets);
    }
}
