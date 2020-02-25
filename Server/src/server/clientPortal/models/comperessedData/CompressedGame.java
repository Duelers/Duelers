package server.clientPortal.models.comperessedData;

import server.gameCenter.models.game.GameType;
import server.gameCenter.models.game.Player;
import server.gameCenter.models.map.GameMap;

public class CompressedGame {
    private CompressedPlayer playerOne;
    private CompressedPlayer playerTwo;
    private CompressedGameMap gameMap;
    private int turnNumber;
    private GameType gameType;

    public CompressedGame(Player playerOne, Player playerTwo, GameMap gameMap, int turnNumber, GameType gameType) {
        this.playerOne = playerOne.toCompressedPlayer();
        this.playerTwo = playerTwo.toCompressedPlayer();
        this.gameMap = gameMap.toCompressedGameMap();
        this.turnNumber = turnNumber;
        this.gameType = gameType;
    }

	public CompressedPlayer getPlayerOne() {
        return playerOne;
    }

    public CompressedPlayer getPlayerTwo() {
        return playerTwo;
    }

    public GameType getGameType() {
        return gameType;
    }
}
