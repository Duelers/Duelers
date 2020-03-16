package shared.models.game.map;

import shared.Constants;
import shared.models.game.Troop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BaseGameMap<TroopType extends Troop> {
    protected static final int NUM_ROWS = Constants.NUMBER_OF_ROWS;
    protected static final int NUM_COLUMNS = Constants.NUMBER_OF_COLUMNS;

    protected final Cell[][] cells;
    protected final List<TroopType> troops = new ArrayList<>();
    protected CellEffect[] cellEffects;

    /**
     * If we want to add items to the board on startup (eg terrain, mana springs, etc)
     * If can be placed here by setting cell[x][y] as the item
     * (0,4) | (2,5) | (4,4) are the correct coordinates for mana springs.
     */
    public BaseGameMap() {
        this.cells = new Cell[NUM_ROWS][NUM_COLUMNS];
        for (int i = 0; i < NUM_ROWS; i++) {
            for (int j = 0; j < NUM_COLUMNS; j++) {
                cells[i][j] = new Cell(i, j);
            }
        }
    }

    public BaseGameMap(Cell[][] cells, List<TroopType> troops) {
        this.cells = new Cell[NUM_ROWS][NUM_COLUMNS];
        for (int i = 0; i < NUM_ROWS; i++) {
            System.arraycopy(cells[i], 0, this.cells[i], 0, NUM_COLUMNS);
        }
        this.troops.addAll(troops);
    }

    public static int getNumRows() {
        return NUM_ROWS;
    }

    public static int getNumColumns() {
        return NUM_COLUMNS;
    }

    public List<TroopType> getTroops() {
        return Collections.unmodifiableList(troops);
    }

    public boolean isInMap(Cell cell) {
        return isInMap(cell.getRow(), cell.getColumn());
    }

    public static boolean isInMap(int row, int column) {
        boolean inLeftBorder = row >= 0;
        boolean inRightBorder = row < NUM_ROWS;
        boolean inTopBorder = column >= 0;
        boolean inBottomBorder = column < NUM_COLUMNS;
        return inLeftBorder && inRightBorder && inTopBorder && inBottomBorder;
    }

    public Cell[][] getCells() {
        return cells;
    }

    public Cell getCell(Cell cell) {
        return cells[cell.getRow()][cell.getColumn()];
    }

    public Cell getCell(int row, int column) {
        if (isInMap(row, column)) {
            return cells[row][column];
        }
        return null;
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
     * Get the manhattan adjacent neighbours cell, not including diagonals.
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

    public List<TroopType> getTroopsBelongingToPlayer(int playerNumber) {
        ArrayList<TroopType> troops = new ArrayList<>();
        for (TroopType troop : this.troops) {
            if (troop.getPlayerNumber() == playerNumber)
                troops.add(troop);
        }
        return troops;
    }

    public TroopType getTroopAtLocation(Cell cell) {
        for (TroopType troop : troops) {
            if (troop.getCell().equals(cell)) {
                return troop;
            }
        }
        return null;
    }

    public TroopType getTroop(String cardId) {
        for (TroopType troop : troops) {
            if (troop.getCard().getCardId().equalsIgnoreCase(cardId)) {
                return troop;
            }
        }
        return null;
    }

    public void addTroop(TroopType troop) {
        this.troops.add(troop);
    }

    public void removeTroop(TroopType troop) {
        troops.remove(troop);
    }

}
