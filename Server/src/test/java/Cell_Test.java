package test.java;


import org.junit.Assert;
import org.junit.Test;
import server.gameCenter.models.map.Cell;

public class Cell_Test {

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
}
