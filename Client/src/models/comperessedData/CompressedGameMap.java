package models.comperessedData;

import models.game.CellEffect;
import models.game.map.Position;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CompressedGameMap {
    private static final int ROW_NUMBER = 5, COLUMN_NUMBER = 9;
    private final CompressedCell[][] cells;
    private final ArrayList<CompressedTroop> troops;
    private CellEffect[] cellEffects;
    private PropertyChangeSupport support = new PropertyChangeSupport(this);

    //just for testing BattleView
    public CompressedGameMap(CompressedCell[][] cells, ArrayList<CompressedTroop> troops) {
        this.cells = cells;
        this.troops = troops;
    }

    public static int getRowNumber() {
        return ROW_NUMBER;
    }

    public static int getColumnNumber() {
        return COLUMN_NUMBER;
    }

    public static boolean isInMap(int row, int column) {
        return row >= 0 && row < ROW_NUMBER && column >= 0 && column < COLUMN_NUMBER;

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

    public CompressedCell[][] getCells() {
        return cells;
    }

    public List<CompressedTroop> getTroops() {
        return Collections.unmodifiableList(troops);
    }

    public CompressedTroop searchTroop(String cardID) {
        for (CompressedTroop troop : troops) {
            if (troop.getCard().getCardId().equalsIgnoreCase(cardID)) {
                return troop;
            }
        }
        return null;
    }

    public void addTroop(CompressedTroop troop) {
        troops.add(troop);
    }

    public List<CompressedTroop> getPlayerTroop(int playerNumber) {
        ArrayList<CompressedTroop> compressedTroops = new ArrayList<>();
        for (CompressedTroop troop : troops) {
            if (troop.getPlayerNumber() == playerNumber)
                compressedTroops.add(troop);
        }
        return compressedTroops;
    }

    public CompressedCell getCell(int row, int column) {
        if (isInMap(row, column)) {
            return cells[row][column];
        }

        return null;
    }

    public CompressedTroop getTroop(Position cell) {
        for (CompressedTroop troop : troops) {
            if (troop.getPosition().equals(cell)) {
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

    void removeItem(String cardId) {
        for (CompressedCell[] row : cells) {
            for (CompressedCell cell : row) {
                support.firePropertyChange("item", cardId, null);
                if (cell.getItem() == null) continue;
                if (cell.getItem().getCardId().equalsIgnoreCase(cardId))
                    cell.removeItem();
            }
        }
    }

    public void addFlagNum(Position position, int addition) {
        cells[position.getRow()][position.getColumn()].addNumberOfFlags(addition);
        support.firePropertyChange("flag", position, cells[position.getRow()][position.getColumn()].getNumberOfFlags());
    }

    public int getFlagNum(Position position) {
        return cells[position.getRow()][position.getColumn()].getNumberOfFlags();//TODO:Ahmad Check
    }

    public void updateTroop(CompressedTroop troop) {
        if (support == null) {
            support = new PropertyChangeSupport(this);
        }
        cells[troop.getPosition().getRow()][troop.getPosition().getColumn()].removeFlags();
        support.firePropertyChange("flag", troop.getPosition(), 0);
        support.firePropertyChange("troop", getTroop(troop.getCard().getCardId()), troop);
        removeTroop(troop.getCard().getCardId());
        if (troop.getCurrentHp() > 0)
            troops.add(troop);
    }

    public void killTroop(String cardId) {//flag
        if (support == null) {
            support = new PropertyChangeSupport(this);
        }
        for (CompressedTroop troop : troops) {
            if (troop.getCard().getCardId().equalsIgnoreCase(cardId)) {
                support.firePropertyChange("troop", troop, null);
                addFlagNum(troop.getPosition(), troop.getNumberOfCollectedFlags());
            }
        }
        removeTroop(cardId);
    }

    private void removeTroop(String cardId) {
        troops.removeIf(compressedTroop -> compressedTroop.getCard().getCardId().equalsIgnoreCase(cardId));
    }

    public CompressedTroop getTroop(String cardId) {
        for (CompressedTroop troop : troops) {
            if (troop.getCard().getCardId().equalsIgnoreCase(cardId)) {
                return troop;
            }
        }
        return null;
    }

    public List<CompressedCell> getFlagCells() {
        ArrayList<CompressedCell> flagCells = new ArrayList<>();
        for (CompressedCell[] row : cells) {
            for (CompressedCell cell : row) {
                int number = cell.getNumberOfFlags();
                while (number-- > 0) {
                    flagCells.add(cell);
                }
            }
        }
        return Collections.unmodifiableList(flagCells);
    }

    public int getCellEffect(int j, int i) {
        if (cellEffects == null)
            return 0;
        for (CellEffect cellEffect : cellEffects) {
            if (cellEffect.getPosition().getRow() == j && cellEffect.getPosition().getColumn() == i) {
                if (cellEffect.isPositive())
                    return +1;
                else
                    return -1;
            }
        }
        return 0;
    }
}
