package server.clientPortal.models.comperessedData;

import server.gameCenter.models.game.ServerTroop;
import shared.models.game.map.Cell;

import java.util.ArrayList;
import java.util.List;

public class CompressedGameMap {
    private static final int ROW_NUMBER = 5, COLUMN_NUMBER = 9;

    public CompressedGameMap(Cell[][] cells, List<ServerTroop> troops) {
        for (int i = 0; i < ROW_NUMBER; i++) {
            Cell[][] cells1 = new Cell[ROW_NUMBER][COLUMN_NUMBER];
            System.arraycopy(cells[i], 0, cells1[i], 0, COLUMN_NUMBER);
        }
        List<ServerTroop> troops1 = new ArrayList<>();
        troops1.addAll(troops);
    }
}
