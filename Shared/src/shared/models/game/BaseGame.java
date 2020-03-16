package shared.models.game;

import shared.models.game.map.BaseGameMap;

public class BaseGame<PlayerType extends BasePlayer, GameMapType extends BaseGameMap> {
    private final PlayerType playerOne;
    private final PlayerType playerTwo;
    private final GameMapType gameMap;
    private final int turnNumber;
    private final GameType gameType;

    public BaseGame(PlayerType playerOne, PlayerType playerTwo, GameMapType gameMap, int turnNumber, GameType gameType) {
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        this.gameMap = gameMap;
        this.turnNumber = turnNumber;
        this.gameType = gameType;
    }

}
