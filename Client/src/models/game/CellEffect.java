package models.game;

import models.game.map.Position;

public class CellEffect {
    private Position position;
    private boolean positive;

    public CellEffect(int row, int column, boolean positive) {
        this.positive = positive;
        position = new Position(row, column);
    }

    public Position getPosition() {
        return position;
    }

    public boolean isPositive() {
        return positive;
    }
}
