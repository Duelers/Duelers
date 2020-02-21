package server.gameCenter.models.game;

import server.gameCenter.models.map.Cell;

public class CellEffect {
    private Cell cell;
    private boolean positive;

    public CellEffect(Cell cell, boolean positive) {
        this.cell = cell;
        this.positive = positive;
    }
}
