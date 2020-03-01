package models.game.availableActions;

import shared.models.game.Troop;
import shared.models.game.map.Cell;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Move {
    private Troop troop;
    private List<Cell> targets;

    Move(Troop troop, ArrayList<Cell> targets) {
        this.troop = troop;
        this.targets = targets;
    }

    public Troop getTroop() {
        return troop;
    }

    List<Cell> getTargets() {
        return Collections.unmodifiableList(targets);
    }
}
