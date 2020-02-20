package localGameCenter.models.game;

import localGameCenter.models.map.Position;

public class CellEffect {
    private Position position;
    private boolean positive;

    public CellEffect(Position position, boolean positive) {
        this.position = position;
        this.positive = positive;
    }
}
