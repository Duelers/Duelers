package shared.models.game.availableactions;

import shared.models.game.Troop;

import java.util.Collections;
import java.util.List;

public class BaseAttack<TroopType extends Troop> {
    protected final TroopType attackerTroop;
    protected final List<TroopType> defenders;

    public BaseAttack(TroopType attackerTroop, List<TroopType> defenders) {
        this.attackerTroop = attackerTroop;
        this.defenders = defenders;
    }

    public TroopType getAttackerTroop() {
        return attackerTroop;
    }

    public List<TroopType> getDefenders() {
        return Collections.unmodifiableList(defenders);
    }
}
