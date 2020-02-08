package models.comperessedData;

import models.game.map.Position;

public class CompressedCell {
    private int row;
    private int column;
    private CompressedCard item;//non flag item
    //just for testing BattleView


    public CompressedCell(int row, int column, CompressedCard item) {
        this.row = row;
        this.column = column;
        this.item = item;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public CompressedCard getItem() {
        return item;
    }

    public void removeItem() {
        item = null;
    }

    public int manhattanDistance(CompressedCell cell) {
        return Math.abs(cell.row - row) + Math.abs(cell.column - column);
    }

    public Position toPosition() {
        return new Position(row, column);
    }
}
