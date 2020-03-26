package shared.models.game.map;

import java.util.Objects;

public class Cell {
  private final int row;
  private final int column;

  public Cell(int row, int column) {
    this.row = row;
    this.column = column;
  }

  public int getRow() {
    return this.row;
  }

  public int getColumn() {
    return this.column;
  }

  public boolean isNearbyCell(Cell cell) {
    return Math.abs(cell.row - row) <= 1 && Math.abs(cell.column - column) <= 1;
  }

  public int manhattanDistance(Cell otherCell) {
    return Math.abs(otherCell.row - row) + Math.abs(otherCell.column - column);
  }

  @Override
  public int hashCode() {
    return Objects.hash(row, column);
  }

  @Override
  public boolean equals(Object obj) {
    if (!obj.getClass().equals(this.getClass()))
      return false;
    Cell cell = (Cell) obj;
    return row == cell.row && column == cell.column;
  }

  @Override
  public String toString() {
    return "Cell: (" + this.getRow() + "," + this.getColumn() + ")";
  }

}
