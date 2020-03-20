package server.clientPortal.models.message;

import server.gameCenter.models.game.Game;

class GameCopyMessage {
    private final Game game;

    GameCopyMessage(Game game) {
        this.game = game;
    }
}
