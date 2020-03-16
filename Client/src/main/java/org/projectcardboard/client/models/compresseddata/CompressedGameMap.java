package org.projectcardboard.client.models.compresseddata;

import shared.models.game.Troop;
import shared.models.game.map.CellEffect;
import shared.models.game.map.Cell;
import shared.models.game.map.BaseGameMap;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

public class CompressedGameMap extends BaseGameMap<Troop> {
    private PropertyChangeSupport support = new PropertyChangeSupport(this);

    //just for testing BattleView
    public CompressedGameMap(Cell[][] cells, ArrayList<Troop> troops) {
        super(cells, troops);
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
