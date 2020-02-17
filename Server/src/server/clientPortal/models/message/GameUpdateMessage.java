package server.clientPortal.models.message;

import server.gameCenter.models.game.CellEffect;

import java.util.List;

class GameUpdateMessage {
    private final int turnNumber;
    private final int player1CurrentMP;
    private final int player2CurrentMP;
    private final List<CellEffect> cellEffects;

    GameUpdateMessage(int turnNumber, int player1CurrentMP,  int player2CurrentMP, List<CellEffect> cellEffects) {
        this.turnNumber = turnNumber;
        this.player1CurrentMP = player1CurrentMP;
        this.player2CurrentMP = player2CurrentMP;
        this.cellEffects = cellEffects;
    }
}
