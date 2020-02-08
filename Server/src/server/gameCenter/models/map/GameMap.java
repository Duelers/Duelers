package server.gameCenter.models.map;

import server.clientPortal.models.comperessedData.CompressedGameMap;
import server.dataCenter.models.card.Card;
import server.gameCenter.models.game.Player;
import server.gameCenter.models.game.Troop;

import java.util.*;

public class GameMap {
    private static final int ROW_NUMBER = 5, COLUMN_NUMBER = 9;
    private Cell[][] cells;
    private List<Troop> troops = new ArrayList<>();

    public GameMap(List<Card> items, Card originalFlag) {
        cells = new Cell[ROW_NUMBER][COLUMN_NUMBER];
        for (int i = 0; i < ROW_NUMBER; i++) {
            for (int j = 0; j < COLUMN_NUMBER; j++) {
                cells[i][j] = new Cell(i, j);
            }
        }
        LinkedList<Card> newItems = new LinkedList<>(items);
        Collections.shuffle(newItems);

        //cells[0][4].addItem(newItems.poll());
        //cells[2][5].addItem(newItems.poll());
        //cells[4][4].addItem(newItems.poll());
    }

    public static int getRowNumber() {
        return ROW_NUMBER;
    }

    public static int getColumnNumber() {
        return COLUMN_NUMBER;
    }

    public CompressedGameMap toCompressedGameMap() {
        return new CompressedGameMap(cells, troops);
    }

    public Cell getCell(Position position) {
        return cells[position.getRow()][position.getColumn()];
    }

    public Cell getCell(int row, int column) {
        if (isInMap(row, column)) {
            return cells[row][column];
        }
        return null;
    }

    public boolean isInMap(int row, int column) {
        return row >= 0 && row < ROW_NUMBER && column >= 0 && column < COLUMN_NUMBER;
    }

    public boolean isInMap(Position position) {
        return position.getRow() >= 0 && position.getRow() < ROW_NUMBER && position.getColumn() >= 0 && position.getColumn() < COLUMN_NUMBER;
    }


    public Cell[][] getCells() {
        return this.cells;
    }


    public void addTroop(int playerNumber, Troop troop) {
        this.troops.add(troop);
    }

    private Troop getTroop(int row, int column) {
        for (Troop troop : troops) {
            if (troop.getCell().getColumn() == column && troop.getCell().getRow() == row) {
                return troop;
            }
        }
        return null;
    }

    public Troop getTroop(Cell cell) {
        return getTroop(cell.getRow(), cell.getColumn());
    }

    public Troop getTroop(Position cell) {
        return getTroop(cell.getRow(), cell.getColumn());
    }

    public Troop getTroop(String cardId) {
        for (Troop troop : troops) {
            if (troop.getCard().getCardId().equalsIgnoreCase(cardId)) {
                return troop;
            }
        }
        return null;
    }

    public void removeTroop(Player player, Troop troop) {
        troops.remove(troop);
    }


    public List<Troop> getTroops() {
        return Collections.unmodifiableList(troops);
    }
}