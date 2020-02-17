package models.message;

import models.game.CellEffect;


public class GameUpdateMessage {
    private int turnNumber;
    private int player1CurrentMP;
    private int player2CurrentMP;
    private CellEffect[] cellEffects;

    public int getTurnNumber() {
        return turnNumber;
    }

    public int getPlayer1CurrentMP() {
        return player1CurrentMP;
    }

    public int getPlayer2CurrentMP() {
        return player2CurrentMP;
    }

    public CellEffect[] getCellEffects() {
        return cellEffects;
    }
}
