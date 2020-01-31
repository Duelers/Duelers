package models.comperessedData;

import models.game.map.Position;

public class CompressedCell {
    private int row;
    private int column;
    private CompressedCard item;//non flag item
    private int numberOfFlags;

    //just for testing BattleView


    public CompressedCell(int row, int column, CompressedCard item, int numberOfFlags) {
        this.row = row;
        this.column = column;
        this.item = item;
        this.numberOfFlags = numberOfFlags;
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

    public int getNumberOfFlags() {
        return numberOfFlags;
    }

    public void addNumberOfFlags(int addition) {
        this.numberOfFlags += addition;
    }

    public void removeFlags() {
        this.numberOfFlags = 0;
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
