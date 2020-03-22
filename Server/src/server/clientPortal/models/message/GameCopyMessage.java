package server.clientPortal.models.message;

import server.gameCenter.models.game.Game;

class GameCopyMessage {
    private final Game game;
    private final int p1StartingDeckSize;
    private final int p2StartingDeckSize;

    GameCopyMessage(Game game) {
        this.game = game;
        this.p1StartingDeckSize = game.getPlayerOne().getDeck().getCards().size();
        this.p2StartingDeckSize = game.getPlayerTwo().getDeck().getCards().size();
    }
    
    public Game getGame() {
        return game;
    }

    public int getP1StartingDeckSize() {
        return p1StartingDeckSize;
    }

    public int getP2StartingDeckSize() {
        return p2StartingDeckSize;
    }


}
