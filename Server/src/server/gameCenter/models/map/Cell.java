package server.gameCenter.models.map;

import java.util.ArrayList;

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

    @Override
    public String toString() {
        return "Cell: (" + this.getRow() + "," + this.getColumn() + ")";
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


    public ArrayList<Cell> getNeighbourCells(int numRows, int numCols) {
        /**
         * Gets the 3x3 Neigbours of a cell.
         * Note that this function assumes a finite plane (i.e there are edges and corners)
         * All indexes >= 0 and less than the number of Rows/Columns
         * Also note that this function does not return the cell itself.
         */

        ArrayList<Cell> cells = new ArrayList<>();

        short[] offsets = {-1, 0, 1};
        for (short i : offsets) {
            for (short j : offsets) {

                int r2 = i + row;
                int c2 = j + column;

                // Check cells bounds, we want all index to be positive and less than the upper bound.
                boolean checkRow = (r2 >= 0) ? r2 < numRows : false;
                boolean checkCol = (c2 >= 0) ? c2 < numCols : false;
                boolean checkIdentity = (r2 == row) ? c2 == column : false; // don't add the current cell itself

                if (checkRow && checkCol && !checkIdentity) {
                    cells.add(new Cell(row + i, column + j));
                }
            }
        }
        return cells;
    }

    public int manhattanDistance(Cell otherCell) {
        return Math.abs(otherCell.row - row) + Math.abs(otherCell.column - column);
    }
}