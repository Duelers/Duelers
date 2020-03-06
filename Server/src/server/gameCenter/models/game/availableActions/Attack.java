package server.gameCenter.models.game.availableActions;

import server.gameCenter.models.game.ServerTroop;

import java.util.Collections;
import java.util.List;

public class Attack {
    private final ServerTroop attackerTroop;
    private final List<ServerTroop> defenders;

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
