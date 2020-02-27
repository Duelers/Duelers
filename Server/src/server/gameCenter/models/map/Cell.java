package server.gameCenter.models.map;

import java.util.ArrayList;
import java.util.Objects;

public class Cell {
    private int row;
    private int column;

    public Cell(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return this.row;
    }

    public int getColumn() {
        return this.column;
    }

    public boolean isNextTo(Cell cell) {
        return Math.abs(cell.row - row) < 2 && Math.abs(cell.column - column) < 2;
    }

    public int manhattanDistance(Cell otherCell) {
        return Math.abs(otherCell.row - row) + Math.abs(otherCell.column - column);
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() != this.getClass()) return false;
        Cell cell = (Cell) obj;
        return row == cell.row && column == cell.column;
    }

    @Override
    public String toString() {
        return "Cell: (" + this.getRow() + "," + this.getColumn() + ")";
    }
}