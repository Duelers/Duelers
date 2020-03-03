package shared.models.game.map;

public class CellEffect {
    private final Cell cell;
    private final boolean positive;

    public CellEffect(Cell cell, boolean positive) {
        this.cell = cell;
        this.positive = positive;
    }

    public Cell getCell() {
        return cell;
    }

    public boolean isPositive() {
        return positive;
    }
}
