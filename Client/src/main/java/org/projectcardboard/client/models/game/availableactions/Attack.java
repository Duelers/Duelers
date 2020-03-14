package org.projectcardboard.client.models.game.availableactions;


import java.util.ArrayList;

import shared.models.game.Troop;
import shared.models.game.availableactions.BaseAttack;

public class Attack extends BaseAttack<Troop> {
    Attack(Troop attackerTroop, ArrayList<Troop> defenders) {
        super(attackerTroop, defenders);
    }
}
