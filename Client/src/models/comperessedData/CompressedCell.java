package models.comperessedData;

import models.game.map.Cell;
import server.gameCenter.models.map.Cell.*;

public class CompressedCell {
    private int row;
    private int column;

    //just for testing BattleView


    public CompressedCell(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public int manhattanDistance(CompressedCell cell) {
        return Math.abs(cell.row - row) + Math.abs(cell.column - column);
    }

    public Cell toCell() {
        return new Cell(row, column);
    }
}
