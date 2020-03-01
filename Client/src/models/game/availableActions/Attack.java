package models.game.availableActions;


import shared.models.card.CompressedTroop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Attack {
    private CompressedTroop attackerTroop;
    private List<CompressedTroop> defenders;

    Attack(CompressedTroop attackerTroop, ArrayList<CompressedTroop> defenders) {
        this.attackerTroop = attackerTroop;
        this.defenders = defenders;
    }

    public CompressedTroop getAttackerTroop() {
        return attackerTroop;
    }

    public List<CompressedTroop> getDefenders() {
        return Collections.unmodifiableList(defenders);
    }
}
