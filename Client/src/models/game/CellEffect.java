package models.game;

import server.gameCenter.models.map.Cell;

public class CellEffect {
    private Cell cell;
    private boolean positive;

    public CellEffect(int row, int column, boolean positive) {
        this.positive = positive;
        cell = new Cell(row, column);
    }

    public Cell getCell() {
        return cell;
    }

    public boolean isPositive() {
        return positive;
    }
}
