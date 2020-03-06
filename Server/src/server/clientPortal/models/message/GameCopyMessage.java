package server.clientPortal.models.message;

import server.clientPortal.models.comperessedData.CompressedGame;
import server.gameCenter.models.game.Game;

class GameCopyMessage {

    GameCopyMessage(Game game) {
        CompressedGame compressedGame = game.toCompressedGame();
    }
}
