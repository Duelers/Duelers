package shared.models.game.availableactions;

import shared.models.game.Troop;
import shared.models.game.map.Cell;

import java.util.Collections;
import java.util.List;

public class BaseMove<TroopType extends Troop> {
  private final TroopType troop;
  private final List<Cell> targets;

  public BaseMove(TroopType troop, List<Cell> targets) {
    this.troop = troop;
    this.targets = targets;
  }

  public TroopType getTroop() {
    return troop;
  }

  public List<Cell> getTargets() {
    return Collections.unmodifiableList(targets);
  }
}
