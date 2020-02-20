package message;

import localGameCenter.models.game.Game;
import localGameCenter.models.game.GameType;
import localGameCenter.models.compressedData.CompressedGame;

public class OnlineGame {
    private String player1;
    private String player2;
    private GameType gameType;

    public OnlineGame(Game game) {
        player1 = game.getPlayerOne().getUserName();
        player2 = game.getPlayerTwo().getUserName();
        gameType = game.getGameType();
    }

    public OnlineGame(CompressedGame game) {
        player1 = game.getPlayerOne().getUserName();
        player2 = game.getPlayerTwo().getUserName();
        gameType = game.getGameType();
    }

    public String getPlayer1() {
        return player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public GameType getGameType() {
        return gameType;
    }
}
