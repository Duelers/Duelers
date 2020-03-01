package server.clientPortal.models.comperessedData;

import shared.models.game.Troop;
import shared.models.game.map.Cell;

import java.util.ArrayList;
import java.util.List;

public class CompressedGameMap {
    private static final int ROW_NUMBER = 5, COLUMN_NUMBER = 9;

    private Cell[][] cells = new Cell[ROW_NUMBER][COLUMN_NUMBER];
    private List<Troop> troops = new ArrayList<>();

    public CompressedGameMap(Cell[][] cells, List<Troop> troops) {
        for (int i = 0; i < ROW_NUMBER; i++) {
            for (int j = 0; j < COLUMN_NUMBER; j++) {
                this.cells[i][j] = cells[i][j];
            }
        }
        this.troops.addAll(troops);
    }
}
