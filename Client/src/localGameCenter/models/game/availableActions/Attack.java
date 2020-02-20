package localGameCenter.models.game.availableActions;

import localGameCenter.models.game.Troop;

import java.util.Collections;
import java.util.List;

public class Attack {
    private Troop attackerTroop;
    private List<Troop> defenders;

    Attack(Troop attackerTroop, List<Troop> defenders) {
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
