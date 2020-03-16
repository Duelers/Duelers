package server.clientPortal.models.comperessedData;

import server.gameCenter.models.game.ServerTroop;
import shared.Constants;
import shared.models.game.map.Cell;
import shared.models.game.map.GameMap;

import java.util.ArrayList;
import java.util.List;

public class CompressedGameMap extends GameMap<ServerTroop> {
    public CompressedGameMap(Cell[][] cells, List<ServerTroop> troops) {
        super(cells, troops);
    }
}
