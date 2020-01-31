package server.gameCenter.models.game;

import server.gameCenter.models.map.Position;

public class CellEffect {
    private Position position;
    private boolean positive;

    public CellEffect(Position position, boolean positive) {
        this.position = position;
        this.positive = positive;
    }
}
