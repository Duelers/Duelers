package server.clientPortal.models.comperessedData;

import server.gameCenter.models.game.ServerTroop;
import shared.models.game.map.Cell;
import shared.models.game.map.BaseGameMap;

import java.util.List;

public class CompressedGameMap extends BaseGameMap<ServerTroop> {
    public CompressedGameMap(Cell[][] cells, List<ServerTroop> troops) {
        super(cells, troops);
    }
}
