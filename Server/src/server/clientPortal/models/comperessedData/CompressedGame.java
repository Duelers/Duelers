package server.clientPortal.models.comperessedData;

import shared.models.game.GameType;
import server.gameCenter.models.game.Player;
import server.gameCenter.models.map.GameMap;

public class CompressedGame {
    private final CompressedPlayer playerOne;
    private final CompressedPlayer playerTwo;
    private final CompressedGameMap gameMap;
    private final int turnNumber;
    private final GameType gameType;

    public CompressedGame(Player playerOne, Player playerTwo, GameMap gameMap, int turnNumber, GameType gameType) {
        this.playerOne = playerOne.toCompressedPlayer();
        this.playerTwo = playerTwo.toCompressedPlayer();
        this.gameMap = gameMap.toCompressedGameMap();
        this.turnNumber = turnNumber;
        this.gameType = gameType;
    }
}
