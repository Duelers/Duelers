package org.projectcardboard.client.models.compresseddata;

import shared.Constants;
import shared.models.game.Troop;
import shared.models.game.map.CellEffect;
import shared.models.game.map.Cell;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CompressedGameMap {
    private static final int NUM_ROWS = Constants.NUMBER_OF_ROWS;
    private static final int NUM_COLUMNS = Constants.NUMBER_OF_COLUMNS;
    private final Cell[][] cells;
    private final ArrayList<Troop> troops;
    private CellEffect[] cellEffects;
    private PropertyChangeSupport support = new PropertyChangeSupport(this);

    //just for testing BattleView
    public CompressedGameMap(Cell[][] cells, ArrayList<Troop> troops) {
        this.cells = cells;
        this.troops = troops;
    }

    public static int getRowNumber() {
        return NUM_ROWS;
    }

    public static int getColumnNumber() {
        return NUM_COLUMNS;
    }

    public static boolean isInMap(int row, int column) {
        return row >= 0 && row < NUM_ROWS && column >= 0 && column < NUM_COLUMNS;
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

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        if (support == null) {
            support = new PropertyChangeSupport(this);
        }
        support.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }

    public Cell[][] getCells() {
        return cells;
    }

    public List<Troop> getTroops() {
        return Collections.unmodifiableList(troops);
    }
    
    public void addTroop(Troop troop) {
        troops.add(troop);
    }

    public List<Troop> getPlayerTroop(int playerNumber) {
        ArrayList<Troop> troops = new ArrayList<>();
        for (Troop troop : this.troops) {
            if (troop.getPlayerNumber() == playerNumber)
                troops.add(troop);
        }
        return troops;
    }

    public Cell getCell(int row, int column) {
        if (isInMap(row, column)) {
            return cells[row][column];
        }
        return null;
    }

    public Troop getTroop(Cell cell) {
        for (Troop troop : troops) {
            if (troop.getCell().equals(cell)) {
                return troop;
            }
        }
        return null;
    }

    public void updateCellEffects(CellEffect[] cellEffects) {
        CellEffect[] old = this.cellEffects;
        this.cellEffects = cellEffects;
        if (support == null) support = new PropertyChangeSupport(this);
        support.firePropertyChange("cellEffect", old, cellEffects);
    }

    public void updateTroop(Troop troop) {
        if (support == null) {
            support = new PropertyChangeSupport(this);
        }
        support.firePropertyChange("troop", getTroop(troop.getCard().getCardId()), troop);
        removeTroop(troop.getCard().getCardId());
//        if (troop.getCurrentHp() > 0)
        troops.add(troop);
    }

    public void killTroop(String cardId) {//flag
        if (support == null) {
            support = new PropertyChangeSupport(this);
        }
        for (Troop troop : troops) {
            if (troop.getCard().getCardId().equalsIgnoreCase(cardId)) {
                support.firePropertyChange("troop", troop, null);
            }
        }
        removeTroop(cardId);
    }

    private void removeTroop(String cardId) {
        troops.removeIf(troop -> troop.getCard().getCardId().equalsIgnoreCase(cardId));
    }

    public Troop getTroop(String cardId) {
        for (Troop troop : troops) {
            if (troop.getCard().getCardId().equalsIgnoreCase(cardId)) {
                return troop;
            }
        }
        return null;
    }

    public int getCellEffect(int j, int i) {
        if (cellEffects == null)
            return 0;
        for (CellEffect cellEffect : cellEffects) {
            if (cellEffect.getCell().getRow() == j && cellEffect.getCell().getColumn() == i) {
                if (cellEffect.isPositive())
                    return +1;
                else
                    return -1;
            }
        }
        return 0;
    }
}
