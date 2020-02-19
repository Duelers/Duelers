package test.java;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import server.gameCenter.models.map.Cell;

import java.util.ArrayList;

public class Cell_Test {

    @Test
    public void cellToStringWorks(){

        Cell cellx = new Cell(3, 4);

        String expected = "Cell: (3,4)";
        String actual = cellx.toString();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void cellEqualityWorks(){
        Cell cellx = new Cell(2, 2);
        Cell celly = new Cell(2, 2);
        Cell cellz = new Cell(3,2);

        Assert.assertTrue(cellx.equals(cellx));
        Assert.assertTrue(cellx.equals(celly));

        Assert.assertFalse(cellx.equals(cellz));
    }

    @Test
    public void manhattenDistance_ReturnsTwo_WhenCellIsTwoAway(){
        Cell cell1 = new Cell(0, 0);
        Cell cell2 = new Cell(0, 2);

        Assert.assertEquals(2, cell1.manhattanDistance(cell2));
        Assert.assertEquals(2, cell2.manhattanDistance(cell1));
    }

    @Test
    public void manhattenDistance_ReturnsTwo_WhenCellIsOneAwayDiagonally(){
        Cell cell1 = new Cell(0, 0);
        Cell cell2 = new Cell(1, 1);

        Assert.assertEquals(2, cell1.manhattanDistance(cell2));
        Assert.assertEquals(2, cell2.manhattanDistance(cell1));
    }

    @Test
    public void manhattenDistance_ReturnsFour_WhenCellIsTwoAwayDiagonally(){
        Cell cell1 = new Cell(2, 2);
        Cell cell2 = new Cell(4, 4);

        Assert.assertEquals(4, cell1.manhattanDistance(cell2));
        Assert.assertEquals(4, cell2.manhattanDistance(cell1));
    }

    @Test
    public void getNeighbourCells_ReturnsThreeCells_WhenInTopLeftCorner(){

        Cell TopLeft = new Cell(0, 0);

        ArrayList<Cell> expected = new ArrayList<>();
        expected.add(new Cell(0, 1));
        expected.add(new Cell(1, 0));
        expected.add(new Cell(1,1));

        ArrayList<Cell> actual = TopLeft.getNeighbourCells(10, 10);

        Assert.assertEquals(3, actual.size());
        expected.forEach(n -> Assert.assertTrue(n.toString() + "not in actual",  actual.contains(n)));
    }

    @Test
    public void getNeighbourCells_ReturnsThreeCells_WhenInBottomRightCorner(){

        Cell bottomRight = new Cell(9, 9);

        ArrayList<Cell> expected = new ArrayList<>();
        expected.add(new Cell(9, 8));
        expected.add(new Cell(8, 9));
        expected.add(new Cell(8,8));

        ArrayList<Cell> actual = bottomRight.getNeighbourCells(10, 10);

        Assert.assertEquals(3, actual.size());
        expected.forEach(n -> Assert.assertTrue(n.toString() + "not in actual",  actual.contains(n)));
    }

    @Test
    public void getNeighbourCells_ReturnsFiveCells_WhenOnEdge(){

        Cell bottomRight = new Cell(0, 1);

        ArrayList<Cell> expected = new ArrayList<>();
        expected.add(new Cell(0, 0));
        expected.add(new Cell(1, 0));
        expected.add(new Cell(1,1));
        expected.add(new Cell(0, 2));
        expected.add(new Cell(1, 2));

        ArrayList<Cell> actual = bottomRight.getNeighbourCells(10, 10);

        Assert.assertEquals(5, actual.size());
        expected.forEach(n -> Assert.assertTrue(n.toString() + "not in actual",  actual.contains(n)));
    }

    @Test
    public void getNeighbourCells_ReturnsEightCells_WhenInCentre(){

        Cell centre = new Cell(1, 1);

        ArrayList<Cell> expected = new ArrayList<>();
        expected.add(new Cell(0, 0));
        expected.add(new Cell(1, 0));
        expected.add(new Cell(0,1));
        expected.add(new Cell(1, 2));
        expected.add(new Cell(2, 1));
        expected.add(new Cell(2, 2));
        expected.add(new Cell(0, 2));
        expected.add(new Cell(2, 0));

        ArrayList<Cell> actual = centre.getNeighbourCells(10, 10);

        Assert.assertEquals(8, actual.size());
        expected.forEach(n -> Assert.assertTrue(n.toString() + "not in actual",  actual.contains(n)));
    }

}
