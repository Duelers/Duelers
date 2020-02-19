package models.game.map;

import models.comperessedData.CompressedCell;
import server.gameCenter.models.map.Cell;

import java.util.ArrayList;

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

    public ArrayList<Position> getNeighbourCells(int numRows, int numCols) {
        /**
         * Gets the 3x3 Neigbours of a cell.
         * Note that this function assumes a finite plane (i.e there are edges and corners)
         * All indexes >= 0 and less than the number of Rows/Columns
         * Also note that this function does not return the cell itself.
         */

        ArrayList<Position> positions = new ArrayList<>();

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
                    positions.add(new Position(row + i, column + j));
                }
            }
        }
        return positions;
    }


}