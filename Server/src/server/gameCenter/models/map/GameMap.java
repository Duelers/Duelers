package server.gameCenter.models.map;

import server.clientPortal.models.comperessedData.CompressedGameMap;
import server.gameCenter.models.game.ServerTroop;
import shared.Constants;
import shared.models.game.map.Cell;

import java.util.*;

public class GameMap {
    private static final int NUM_ROWS = Constants.NUMBER_OF_ROWS, NUM_COLUMNS = Constants.NUMBER_OF_COLUMNS;
    private final Cell[][] cells;
    private final List<ServerTroop> troops = new ArrayList<>();

    public GameMap() {
        cells = new Cell[NUM_ROWS][NUM_COLUMNS];
        for (int i = 0; i < NUM_ROWS; i++) {
            for (int j = 0; j < NUM_COLUMNS; j++) {
                cells[i][j] = new Cell(i, j);
            }
        }

        // If we want to add items to the board on startup (eg terrain, mana springs, etc)
        // If can be placed here by setting cell[x][y] as the item
        // (0,4) | (2,5) | (4,4) are the correct coordinates for mana springs.
    }

    public static int getNumRows() {
        return NUM_ROWS;
    }

    public static int getNumColumns() {
        return NUM_COLUMNS;
    }

    public CompressedGameMap toCompressedGameMap() {
        return new CompressedGameMap(cells, troops);
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

    public boolean isInMap(int row, int column) {
        return row >= 0 && row < NUM_ROWS && column >= 0 && column < NUM_COLUMNS;
    }

    public boolean isInMap(Cell cell) {
        return cell.getRow() >= 0 && cell.getRow() < NUM_ROWS && cell.getColumn() >= 0 && cell.getColumn() < NUM_COLUMNS;
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

    public Cell[][] getCells() {
        return this.cells;
    }


    public void addTroop(int playerNumber, ServerTroop troop) {
        this.troops.add(troop);
    }

    private ServerTroop getTroop(int row, int column) {
        for (ServerTroop troop : troops) {
            if (troop.getCell().getColumn() == column && troop.getCell().getRow() == row) {
                return troop;
            }
        }
        return null;
    }

    public ServerTroop getTroop(Cell cell) {
        return getTroop(cell.getRow(), cell.getColumn());
    }


    public ServerTroop getTroop(String cardId) {
        for (ServerTroop troop : troops) {
            if (troop.getCard().getCardId().equalsIgnoreCase(cardId)) {
                return troop;
            }
        }
        return null;
    }

    public void removeTroop(ServerTroop troop) {
        troops.remove(troop);
    }

    public List<ServerTroop> getTroops() {
        return Collections.unmodifiableList(troops);
    }
}