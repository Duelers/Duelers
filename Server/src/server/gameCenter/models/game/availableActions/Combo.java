package server.gameCenter.models.game.availableActions;

import server.gameCenter.models.game.Troop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Combo {
    private List<Troop> attackers;
    private Troop defenderTroop;

    Combo(ArrayList<Troop> attackers, Troop defenderTroop) {
        this.attackers = attackers;
        this.defenderTroop = defenderTroop;
    }

    public List<Troop> getAttackers() {
        return Collections.unmodifiableList(attackers);
    }

    public Troop getDefenderTroop() {
        return defenderTroop;
    }
}
