package test.java;


import org.junit.Assert;
import org.junit.Test;
import server.gameCenter.models.map.Cell;

public class Cell_Test {

    @Test
    public void cellToStringWorks() {

        Cell cellx = new Cell(3, 4);

        String expected = "Cell: (3,4)";
        String actual = cellx.toString();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void cellEqualityWorks() {
        Cell cellx = new Cell(2, 2);
        Cell celly = new Cell(2, 2);
        Cell cellz = new Cell(3, 2);

        Assert.assertTrue(cellx.equals(cellx));
        Assert.assertTrue(cellx.equals(celly));

        Assert.assertFalse(cellx.equals(cellz));
    }

    @Test
    public void manhattenDistance_ReturnsTwo_WhenCellIsTwoAway() {
        Cell cell1 = new Cell(0, 0);
        Cell cell2 = new Cell(0, 2);

        Assert.assertEquals(2, cell1.manhattanDistance(cell2));
        Assert.assertEquals(2, cell2.manhattanDistance(cell1));
    }

    @Test
    public void manhattenDistance_ReturnsTwo_WhenCellIsOneAwayDiagonally() {
        Cell cell1 = new Cell(0, 0);
        Cell cell2 = new Cell(1, 1);

        Assert.assertEquals(2, cell1.manhattanDistance(cell2));
        Assert.assertEquals(2, cell2.manhattanDistance(cell1));
    }

    @Test
    public void manhattenDistance_ReturnsFour_WhenCellIsTwoAwayDiagonally() {
        Cell cell1 = new Cell(2, 2);
        Cell cell2 = new Cell(4, 4);

        Assert.assertEquals(4, cell1.manhattanDistance(cell2));
        Assert.assertEquals(4, cell2.manhattanDistance(cell1));
    }

}
