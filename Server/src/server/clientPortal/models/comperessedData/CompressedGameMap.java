package server.clientPortal.models.comperessedData;

import server.gameCenter.models.game.ServerTroop;
import shared.Constants;
import shared.models.game.map.Cell;

import java.util.ArrayList;
import java.util.List;

public class CompressedGameMap {
    private static final int ROW_NUMBER = Constants.NUMBER_OF_ROWS, COLUMN_NUMBER = Constants.NUMBER_OF_COLUMNS;

    private final Cell[][] cells = new Cell[ROW_NUMBER][COLUMN_NUMBER];
    private final List<ServerTroop> troops = new ArrayList<>();

    public CompressedGameMap(Cell[][] cells, List<ServerTroop> troops) {
        for (int i = 0; i < ROW_NUMBER; i++) {
            System.arraycopy(cells[i], 0, this.cells[i], 0, COLUMN_NUMBER);
        }
        this.troops.addAll(troops);
    }
}
