package models.game.map;

import models.comperessedData.CompressedCell;

public class Position {
    private int row;
    private int column;

    public Position(int row, int column) {
        this.row = row;
        this.column = column;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return row == position.row &&
                column == position.column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public int manhattanDistance(Position position) {
        return Math.abs(row - position.row) + Math.abs(column - position.column);
    }

    public int manhattanDistance(CompressedCell cell) {
        return Math.abs(cell.getRow() - row) + Math.abs(cell.getColumn() - column);
    }

    public int manhattanDistance(int selectedRow, int selectedColumn) {
        return Math.abs(selectedRow - this.row) + Math.abs(selectedColumn - this.column);
    }

    public boolean isNextTo(Position position) {
        return Math.abs(position.row - row) < 2 && Math.abs(position.column - column) < 2;
    }

    @Override
    public String toString() {
        return "(" + row + ", " + column + ")";
    }
}