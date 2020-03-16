package server.gameCenter.models.map;

import server.clientPortal.models.comperessedData.CompressedGameMap;
import server.gameCenter.models.game.ServerTroop;
import shared.models.game.map.BaseGameMap;
import shared.models.game.map.Cell;

public class GameMap extends BaseGameMap<ServerTroop> {
    public GameMap() {
        super();
    }

    public CompressedGameMap toCompressedGameMap() {
        return new CompressedGameMap(cells, troops);
    }
}