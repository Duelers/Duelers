package server.gameCenter.models.game.availableActions;

import server.gameCenter.models.game.ServerTroop;
import shared.models.game.availableactions.BaseMove;
import shared.models.game.map.Cell;

import java.util.ArrayList;

public class Move extends BaseMove<ServerTroop> {
    public Move(ServerTroop troop, ArrayList<Cell> targets) {
        super(troop, targets);
    }
}
