package server.clientPortal.models.comperessedData;

import shared.models.game.GameType;
import server.gameCenter.models.game.Player;
import server.gameCenter.models.map.GameMap;

public class CompressedGame {

    public CompressedGame(Player playerOne, Player playerTwo, GameMap gameMap, int turnNumber, GameType gameType) {
        CompressedPlayer playerOne1 = playerOne.toCompressedPlayer();
        CompressedPlayer playerTwo1 = playerTwo.toCompressedPlayer();
        CompressedGameMap gameMap1 = gameMap.toCompressedGameMap();
    }
}
