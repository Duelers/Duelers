package server.gameCenter.models.map;

import server.clientPortal.models.comperessedData.CompressedGameMap;
import server.gameCenter.models.game.ServerTroop;
import shared.models.game.map.BaseGameMap;
import shared.models.game.map.Cell;

import java.util.*;

public class GameMap extends BaseGameMap<ServerTroop> {
    public GameMap() {
        super();
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
}