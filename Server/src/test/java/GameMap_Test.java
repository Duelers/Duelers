package test.java;


import org.junit.Assert;
import org.junit.Test;
import server.gameCenter.models.map.Cell;
import server.gameCenter.models.map.GameMap;

import java.util.ArrayList;

public class GameMap_Test {

    @Test
    public void getNearbyCells_ReturnsThreeCells_WhenInTopLeftCorner() {
        GameMap map = new GameMap();

        Cell topLeft = new Cell(0, 0);

        ArrayList<Cell> expected = new ArrayList<>();
        expected.add(new Cell(0, 1));
        expected.add(new Cell(1, 0));
        expected.add(new Cell(1, 1));

        ArrayList<Cell> actual = map.getNearbyCells(topLeft);

        Assert.assertEquals(3, actual.size());
        expected.forEach(n -> Assert.assertTrue(n.toString() + "not in actual", actual.contains(n)));
    }

    @Test
    public void getNearbyCells_ReturnsThreeCells_WhenInBottomRightCorner() {
        GameMap map = new GameMap();
        Cell bottomRight = new Cell(GameMap.getNumRows()-1, GameMap.getNumColumns()-1);

        ArrayList<Cell> expected = new ArrayList<>();
        expected.add(new Cell(3, 7));
        expected.add(new Cell(3, 8));
        expected.add(new Cell(4, 7));

        ArrayList<Cell> actual = map.getNearbyCells(bottomRight);

        Assert.assertEquals(3, actual.size());
        expected.forEach(n -> Assert.assertTrue(n.toString() + "not in actual", actual.contains(n)));
    }

    @Test
    public void getNearbyCells_ReturnsFiveCells_WhenOnEdge() {
        GameMap map = new GameMap();
        Cell edge = new Cell(0, 1);

        ArrayList<Cell> expected = new ArrayList<>();
        expected.add(new Cell(0, 0));
        expected.add(new Cell(1, 0));
        expected.add(new Cell(1, 1));
        expected.add(new Cell(0, 2));
        expected.add(new Cell(1, 2));

        ArrayList<Cell> actual = map.getNearbyCells(edge);

        Assert.assertEquals(5, actual.size());
        expected.forEach(n -> Assert.assertTrue(n.toString() + "not in actual", actual.contains(n)));
    }

    @Test
    public void getNearbyCells_ReturnsEightCells_WhenInCentre() {
        GameMap map = new GameMap();
        Cell centre = new Cell(1, 1);

        ArrayList<Cell> expected = new ArrayList<>();
        expected.add(new Cell(0, 0));
        expected.add(new Cell(1, 0));
        expected.add(new Cell(0, 1));
        expected.add(new Cell(1, 2));
        expected.add(new Cell(2, 1));
        expected.add(new Cell(2, 2));
        expected.add(new Cell(0, 2));
        expected.add(new Cell(2, 0));

        ArrayList<Cell> actual = map.getNearbyCells(centre);

        Assert.assertEquals(8, actual.size());
        expected.forEach(n -> Assert.assertTrue(n.toString() + "not in actual", actual.contains(n)));
    }

}
