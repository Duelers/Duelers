package server.gameCenter.models.game.availableActions;

import server.gameCenter.models.game.ServerTroop;
import shared.models.game.availableactions.BaseAttack;

import java.util.Collections;
import java.util.List;

public class Attack extends BaseAttack<ServerTroop> {
    Attack(ServerTroop attackerTroop, List<ServerTroop> defenders) {
        super(attackerTroop, defenders);
    }
}
