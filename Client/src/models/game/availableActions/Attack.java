package models.game.availableActions;


import shared.models.game.Troop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Attack {
    private Troop attackerTroop;
    private List<Troop> defenders;

    Attack(Troop attackerTroop, ArrayList<Troop> defenders) {
        this.attackerTroop = attackerTroop;
        this.defenders = defenders;
    }

    public Troop getAttackerTroop() {
        return attackerTroop;
    }

    public List<Troop> getDefenders() {
        return Collections.unmodifiableList(defenders);
    }
}
