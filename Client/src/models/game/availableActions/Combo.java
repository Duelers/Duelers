package models.game.availableActions;


import models.comperessedData.CompressedTroop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Combo {
    private List<CompressedTroop> attackers;
    private CompressedTroop defenderTroop;

    Combo(ArrayList<CompressedTroop> attackers, CompressedTroop defenderTroop) {
        this.attackers = attackers;
        this.defenderTroop = defenderTroop;
    }

    public List<CompressedTroop> getAttackers() {
        return Collections.unmodifiableList(attackers);
    }

    public CompressedTroop getDefenderTroop() {
        return defenderTroop;
    }
}
