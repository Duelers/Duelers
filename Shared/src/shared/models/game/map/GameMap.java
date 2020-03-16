package shared.models.game.map;

import shared.Constants;
import shared.models.game.Troop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameMap<TroopType extends Troop> {
    protected static final int NUM_ROWS = Constants.NUMBER_OF_ROWS;
    protected static final int NUM_COLUMNS = Constants.NUMBER_OF_COLUMNS;

    protected final Cell[][] cells = new Cell[NUM_ROWS][NUM_COLUMNS];
    protected final List<TroopType> troops = new ArrayList<>();
    protected CellEffect[] cellEffects;

    public GameMap(Cell[][] cells, List<TroopType> troops) {
        for (int i = 0; i < NUM_ROWS; i++) {
            System.arraycopy(cells[i], 0, this.cells[i], 0, NUM_COLUMNS);
        }
        this.troops.addAll(troops);
    }

    public static int getRowNumber() {
        return NUM_ROWS;
    }

    public static int getColumnNumber() {
        return NUM_COLUMNS;
    }

    public Cell[][] getCells() {
        return cells;
    }

    public List<Troop> getTroops() {
        return Collections.unmodifiableList(troops);
    }

    public static boolean isInMap(int row, int column) {
        boolean inLeftBorder = row >= 0;
        boolean inRightBorder = row < NUM_ROWS;
        boolean inTopBorder = column >= 0;
        boolean inBottomBorder = column < NUM_COLUMNS;
        return inLeftBorder && inRightBorder && inTopBorder && inBottomBorder;
    }

    /**
     * Get the 3x3 neighbours of a cell.
     * Do not return the cell itself.
     */
    public ArrayList<Cell> getNearbyCells(Cell cell) {
        ArrayList<Cell> cells = new ArrayList<>();
        short[] offsets = {-1, 0, 1};
        for (short row_offset : offsets) {
            for (short column_offset : offsets) {
                if (row_offset == 0 && column_offset == 0) {
                    continue; // This is the starting cell.
                }
                int new_row = row_offset + cell.getRow();
                int new_column = column_offset + cell.getColumn();

                if (isInMap(new_row, new_column)) {
                    cells.add(new Cell(new_row, new_column));
                }
            }
        }
        return cells;
    }

    /**
     * Get the manhattan adjacent neighbours cell, not including diagonals..
     * Do not return the cell itself.
     */
    public ArrayList<Cell> getManhattanAdjacentCells(Cell cell) {
        ArrayList<Cell> cells = new ArrayList<>();

        short[][] offsets = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (short[] offset : offsets) {
            int new_row = offset[0] + cell.getRow();
            int new_column = offset[1] + cell.getColumn();
            if (isInMap(new_row, new_column)) {
                cells.add(new Cell(new_row, new_column));
            }
        }
        return cells;
    }

}
