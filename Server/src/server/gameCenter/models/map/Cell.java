package server.gameCenter.models.map;

import server.clientPortal.models.comperessedData.CompressedCell;
import server.dataCenter.models.card.Card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Cell {
    private int row;
    private int column;
    private List<Card> items = new ArrayList<>();

    public Cell(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public CompressedCell toCompressedCell() {
        return new CompressedCell(row, column, items);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() != this.getClass()) return false;
        Cell cell = (Cell) obj;
        return row == cell.row && column == cell.column;
    }

    public int getRow() {
        return this.row;
    }

    public int getColumn() {
        return this.column;
    }


    public List<Card> getItems() {
        return Collections.unmodifiableList(items);
    }

    public void clearItems() {
        items.clear();
    }

    void addItem(Card item) {
        this.items.add(item);
    }

    public boolean isNextTo(Cell cell) {
        return Math.abs(cell.row - row) < 2 && Math.abs(cell.column - column) < 2;
    }

    public int manhattanDistance(Cell cell) {
        return Math.abs(cell.row - row) + Math.abs(cell.column - column);
    }

    public int manhattanDistance(Position position) {
        return Math.abs(position.getRow() - this.row) + Math.abs(position.getColumn() - this.column);

    }
}