package org.projectcardboard.client.models.game.availableactions;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import shared.models.game.Troop;

public class Attack {
    private final Troop attackerTroop;
    private final List<Troop> defenders;

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
