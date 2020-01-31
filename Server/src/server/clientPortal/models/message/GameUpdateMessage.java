package server.clientPortal.models.message;

import server.gameCenter.models.game.CellEffect;

import java.util.List;

class GameUpdateMessage {
    private final int turnNumber;
    private final int player1CurrentMP;
    private final int player1NumberOfCollectedFlags;
    private final int player2CurrentMP;
    private final int player2NumberOfCollectedFlags;
    private final List<CellEffect> cellEffects;

    GameUpdateMessage(int turnNumber, int player1CurrentMP, int player1NumberOfCollectedFlags, int player2CurrentMP, int player2NumberOfCollectedFlags, List<CellEffect> cellEffects) {
        this.turnNumber = turnNumber;
        this.player1CurrentMP = player1CurrentMP;
        this.player1NumberOfCollectedFlags = player1NumberOfCollectedFlags;
        this.player2CurrentMP = player2CurrentMP;
        this.player2NumberOfCollectedFlags = player2NumberOfCollectedFlags;
        this.cellEffects = cellEffects;
    }
}
