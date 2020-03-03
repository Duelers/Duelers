package server.clientPortal.models.comperessedData;

import shared.models.game.ServerTroop;
import shared.models.game.map.Cell;

import java.util.ArrayList;
import java.util.List;

public class CompressedGameMap {
    private static final int ROW_NUMBER = 5, COLUMN_NUMBER = 9;

    private Cell[][] cells = new Cell[ROW_NUMBER][COLUMN_NUMBER];
    private List<ServerTroop> troops = new ArrayList<>();

    public CompressedGameMap(Cell[][] cells, List<ServerTroop> troops) {
        for (int i = 0; i < ROW_NUMBER; i++) {
            System.arraycopy(cells[i], 0, this.cells[i], 0, COLUMN_NUMBER);
        }
        this.troops.addAll(troops);
    }
}
