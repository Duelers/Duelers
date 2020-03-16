package server.clientPortal.models.comperessedData;

import shared.models.game.BaseGame;
import shared.models.game.GameType;
import server.gameCenter.models.game.Player;
import server.gameCenter.models.map.GameMap;

public class CompressedGame extends BaseGame<Player, GameMap> {
    public CompressedGame(Player playerOne, Player playerTwo, GameMap gameMap, int turnNumber, GameType gameType) {
        super(playerOne, playerTwo, gameMap, turnNumber, gameType);
    }
}
