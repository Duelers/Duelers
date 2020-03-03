package server.gameCenter.models.game.availableActions;

import shared.models.game.ServerTroop;

import java.util.Collections;
import java.util.List;

public class Attack {
    private ServerTroop attackerTroop;
    private List<ServerTroop> defenders;

    Attack(ServerTroop attackerTroop, List<ServerTroop> defenders) {
        this.attackerTroop = attackerTroop;
        this.defenders = defenders;
    }

    public ServerTroop getAttackerTroop() {
        return attackerTroop;
    }

    public List<ServerTroop> getDefenders() {
        return Collections.unmodifiableList(defenders);
    }
}
