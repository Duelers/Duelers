package server.clientPortal.models.comperessedData;

import shared.models.game.GameType;
import server.gameCenter.models.game.Player;
import server.gameCenter.models.map.GameMap;

public class CompressedGame {
    private final Player playerOne;
    private final int playerOneDeckSize;
    private final Player playerTwo;
    private final int playerTwoDeckSize;
    private final GameMap gameMap;
    private final int turnNumber;
    private final GameType gameType;
    

    public CompressedGame(Player playerOne, Player playerTwo, GameMap gameMap, int turnNumber, GameType gameType) {
        this.playerOne = playerOne;
        this.playerOneDeckSize = playerOne.getDeck().getCards().size();
        this.playerTwo = playerTwo;
        this.playerTwoDeckSize = playerTwo.getDeck().getCards().size();
        this.gameMap = gameMap;
        this.turnNumber = turnNumber;
        this.gameType = gameType;
    }
}
