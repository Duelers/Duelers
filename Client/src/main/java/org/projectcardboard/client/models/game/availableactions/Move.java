package org.projectcardboard.client.models.game.availableactions;

import java.util.ArrayList;

import shared.models.game.Troop;
import shared.models.game.availableactions.BaseMove;
import shared.models.game.map.Cell;

public class Move extends BaseMove<Troop> {
  public Move(Troop troop, ArrayList<Cell> targets) {
    super(troop, targets);
  }
}
