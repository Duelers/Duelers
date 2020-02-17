package models.game.map;

public class Cell {
    private int row;
    private int column;

    public Cell(int row, int column) {
        this.row = row;
        this.column = column;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() != this.getClass()) return false;
        Cell cell = (Cell) obj;
        return row == cell.row && column == cell.column;
    }

    public int getRow() {
        return this.row;
    }

    public int getColumn() {
        return this.column;
    }

    public boolean isNextTo(Cell otherCell) {
        return Math.abs(otherCell.row - row) < 2 && Math.abs(otherCell.column - column) < 2;
    }

    public int manhattanDistance(Cell otherCell) {

        return Math.abs(otherCell.row - row) + Math.abs(otherCell.column - column);
    }

    public int manhattanDistance(int selectedRow, int selectedColumn) {
        return Math.abs(selectedRow - this.row) + Math.abs(selectedColumn - this.column);
    }

    public int manhattanDistance(Position position) {
        return Math.abs(position.getRow() - this.row) + Math.abs(position.getColumn() - this.column);
    }
}