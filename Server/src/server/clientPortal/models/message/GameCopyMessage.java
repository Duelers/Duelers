package server.clientPortal.models.message;

import server.clientPortal.models.comperessedData.CompressedGame;
import server.gameCenter.models.game.Game;

class GameCopyMessage {
    private final CompressedGame compressedGame;

    GameCopyMessage(Game game) {
        this.compressedGame = game.toCompressedGame();
    }
}
