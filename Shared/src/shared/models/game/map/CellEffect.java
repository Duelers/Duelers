package shared.models.game.map;

public class CellEffect {
    private Cell cell;
    private boolean positive;

    public CellEffect(Cell cell, boolean positive) {
        this.cell = cell;
        this.positive = positive;
    }

    public CellEffect(int row, int column, boolean positive) {
        cell = new Cell(row, column);
        this.positive = positive;
    }


    public Cell getCell() {
        return cell;
    }

    public boolean isPositive() {
        return positive;
    }
}
