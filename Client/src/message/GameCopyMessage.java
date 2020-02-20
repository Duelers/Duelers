package message;

import localGameCenter.models.compressedData.CompressedGame;
import localGameCenter.models.game.Game;

class GameCopyMessage {
    private CompressedGame compressedGame;

    GameCopyMessage(Game game) {
        this.compressedGame = game.toCompressedGame();
    }

    public CompressedGame getCompressedGame() {
        return compressedGame;
    }
}
