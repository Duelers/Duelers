package test.java.models.game.map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import shared.models.card.Card;
import shared.models.game.Troop;
import shared.models.game.map.BaseGameMap;
import shared.models.game.map.Cell;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class BaseGameMap_Test {

    static class CellSorter implements Comparator<Cell> {
        public int compare(Cell a, Cell b) {
            int aScore = a.getRow() * 100 + a.getColumn();
            int bScore = b.getRow() * 100 + b.getColumn();
            return aScore - bScore;
        }
    }

    CellSorter cellSorter = new CellSorter();

    Cell topLeft = new Cell(0, 0);
    Cell bottomRight = new Cell(BaseGameMap.getNumRows() - 1, BaseGameMap.getNumColumns() - 1);
    Cell leftEdge = new Cell(0, 1);
    Cell centre = new Cell(1, 1);


    @Mock
    Troop mockFriendlyTroop;
    @Mock
    Troop mockEnemyTroop;

    @Mock
    Card mockFriendlyTroopCard;
    @Mock
    Card mockEnemyTroopCard;

    String friendlyTroopId = "friendlyId";
    String enemyTroopId = "enemyId";

    int friendlyPlayerNumber = 1;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        when(mockFriendlyTroop.getCell()).thenReturn(new Cell(0, 0));
        when(mockFriendlyTroop.getCard()).thenReturn(mockFriendlyTroopCard);
        when(mockFriendlyTroop.getPlayerNumber()).thenReturn(friendlyPlayerNumber);
        when(mockFriendlyTroopCard.getCardId()).thenReturn(friendlyTroopId);

        when(mockEnemyTroop.getCell()).thenReturn(new Cell(1, 1));
        when(mockEnemyTroop.getCard()).thenReturn(mockEnemyTroopCard);
        when(mockEnemyTroop.getPlayerNumber()).thenReturn(friendlyPlayerNumber + 1);
        when(mockEnemyTroopCard.getCardId()).thenReturn(enemyTroopId);
    }


    @Test
    public void isInMap_center_true() {
        Cell cell = new Cell(3, 3);
        assertTrue(BaseGameMap.isInMap(cell));
    }

    @Test
    public void isInMap_topLeft_true() {
        Cell cell = new Cell(0, 0);
        assertTrue(BaseGameMap.isInMap(cell));
    }

    @Test
    public void isInMap_topRight_true() {
        Cell cell = new Cell(0, BaseGameMap.getNumColumns() - 1);
        assertTrue(BaseGameMap.isInMap(cell));
    }

    @Test
    public void isInMap_bottomLeft_true() {
        Cell cell = new Cell(BaseGameMap.getNumRows() - 1, 0);
        assertTrue(BaseGameMap.isInMap(cell));
    }

    @Test
    public void isInMap_bottomRight_true() {
        Cell cell = new Cell(BaseGameMap.getNumRows() - 1, BaseGameMap.getNumColumns() - 1);
        assertTrue(BaseGameMap.isInMap(cell));
    }

    @Test
    public void isInMap_tooFarLeft_false() {
        Cell cell = new Cell(3, -1);
        assertFalse(BaseGameMap.isInMap(cell));
    }

    @Test
    public void isInMap_tooFarRight_false() {
        Cell cell = new Cell(3, BaseGameMap.getNumColumns());
        assertFalse(BaseGameMap.isInMap(cell));
    }

    @Test
    public void isInMap_tooFarUp_false() {
        Cell cell = new Cell(-1, 3);
        assertFalse(BaseGameMap.isInMap(cell));
    }

    @Test
    public void isInMap_tooFarDown_false() {
        Cell cell = new Cell(BaseGameMap.getNumRows(), 3);
        assertFalse(BaseGameMap.isInMap(cell));
    }


    @Test
    public void getNearbyCells_ReturnsThreeCells_WhenInTopLeftCorner() {
        BaseGameMap<Troop> map = new BaseGameMap<>();

        ArrayList<Cell> expected = new ArrayList<>();
        expected.add(new Cell(0, 1));
        expected.add(new Cell(1, 0));
        expected.add(new Cell(1, 1));

        ArrayList<Cell> actual = map.getNearbyCells(topLeft);

        expected.sort(cellSorter);
        actual.sort(cellSorter);
        assertEquals(expected, actual);
    }

    @Test
    public void getNearbyCells_ReturnsThreeCells_WhenInBottomRightCorner() {
        BaseGameMap<Troop> map = new BaseGameMap<>();

        ArrayList<Cell> expected = new ArrayList<>();
        expected.add(new Cell(3, 7));
        expected.add(new Cell(3, 8));
        expected.add(new Cell(4, 7));

        ArrayList<Cell> actual = map.getNearbyCells(bottomRight);

        expected.sort(cellSorter);
        actual.sort(cellSorter);
        assertEquals(expected, actual);
    }

    @Test
    public void getNearbyCells_ReturnsFiveCells_WhenOnEdge() {
        BaseGameMap<Troop> map = new BaseGameMap<>();
        Cell edge = new Cell(0, 1);

        ArrayList<Cell> expected = new ArrayList<>();
        expected.add(new Cell(0, 0));
        expected.add(new Cell(1, 0));
        expected.add(new Cell(1, 1));
        expected.add(new Cell(0, 2));
        expected.add(new Cell(1, 2));

        ArrayList<Cell> actual = map.getNearbyCells(edge);

        expected.sort(cellSorter);
        actual.sort(cellSorter);
        assertEquals(expected, actual);
    }

    @Test
    public void getNearbyCells_ReturnsEightCells_WhenInCentre() {
        BaseGameMap<Troop> map = new BaseGameMap<>();

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

        expected.sort(cellSorter);
        actual.sort(cellSorter);
        assertEquals(expected, actual);
    }


    @Test
    public void getManhattanAdjacentCells_WhenInTopLeftCorner_ReturnsCells() {
        BaseGameMap<Troop> map = new BaseGameMap<>();

        ArrayList<Cell> expected = new ArrayList<>();
        expected.add(new Cell(0, 1));
        expected.add(new Cell(1, 0));

        ArrayList<Cell> actual = map.getManhattanAdjacentCells(topLeft);

        expected.sort(cellSorter);
        actual.sort(cellSorter);
        assertEquals(expected, actual);
    }

    @Test
    public void getManhattanAdjacentCells_WhenInBottomRightCorner_ReturnsCells() {
        BaseGameMap<Troop> map = new BaseGameMap<>();

        ArrayList<Cell> expected = new ArrayList<>();
        expected.add(new Cell(3, 8));
        expected.add(new Cell(4, 7));

        ArrayList<Cell> actual = map.getManhattanAdjacentCells(bottomRight);

        expected.sort(cellSorter);
        actual.sort(cellSorter);
        assertEquals(expected, actual);
    }

    @Test
    public void getManhattanAdjacentCells_WhenOnEdge_ReturnsCells() {
        BaseGameMap<Troop> map = new BaseGameMap<>();

        ArrayList<Cell> expected = new ArrayList<>();
        expected.add(new Cell(0, 0));
        expected.add(new Cell(1, 1));
        expected.add(new Cell(0, 2));

        ArrayList<Cell> actual = map.getManhattanAdjacentCells(leftEdge);

        expected.sort(cellSorter);
        actual.sort(cellSorter);
        assertEquals(expected, actual);
    }

    @Test
    public void getManhattanAdjacentCells_WhenInCentre_ReturnsCells() {
        BaseGameMap<Troop> map = new BaseGameMap<>();

        ArrayList<Cell> expected = new ArrayList<>();
        expected.add(new Cell(0, 1));
        expected.add(new Cell(1, 0));
        expected.add(new Cell(1, 2));
        expected.add(new Cell(2, 1));

        ArrayList<Cell> actual = map.getManhattanAdjacentCells(centre);

        expected.sort(cellSorter);
        actual.sort(cellSorter);
        assertEquals(expected, actual);
    }


    @Test
    public void getTroopsBelongingToPlayer_returnsOnlyFriendlyTroops() {
        BaseGameMap<Troop> sut = new BaseGameMap<>();
        sut.addTroop(mockFriendlyTroop);
        sut.addTroop(mockEnemyTroop);

        ArrayList<Troop> expected = new ArrayList<>();
        expected.add(mockFriendlyTroop);

        ArrayList<Troop> actual = sut.getTroopsBelongingToPlayer(1);
        assertEquals(expected, actual);
    }

    @Test
    public void getTroopAtLocation_whenNoTroopAtLocation_returnsNull() {
        BaseGameMap<Troop> sut = new BaseGameMap<>();

        assertNull(sut.getTroopAtLocation(new Cell(0, 0)));
    }

    @Test
    public void getTroopAtLocation_whenTroopAtLocation_returnsTroop() {
        BaseGameMap<Troop> sut = new BaseGameMap<>();
        sut.addTroop(mockFriendlyTroop);

        ArrayList<Troop> expected = new ArrayList<>();
        expected.add(mockFriendlyTroop);

        ArrayList<Troop> actual = sut.getTroopsBelongingToPlayer(1);
        assertEquals(expected, actual);
    }


    @Test
    public void getTroop_whenNoTroopWithId_returnsNull() {
        BaseGameMap<Troop> sut = new BaseGameMap<>();

        assertNull(sut.getTroop("someBadId"));
    }

    @Test
    public void getTroop_whenTroopWithId_returnsTroop() {
        BaseGameMap<Troop> sut = new BaseGameMap<>();
        sut.addTroop(mockFriendlyTroop);

        Troop actual = sut.getTroop(friendlyTroopId);
        Troop expected = mockFriendlyTroop;

        assertEquals(expected, actual);
    }


    @Test
    public void removeTroop() {
        BaseGameMap<Troop> sut = new BaseGameMap<>();
        sut.addTroop(mockFriendlyTroop);
        sut.addTroop(mockEnemyTroop);

        List<Troop> expected = new ArrayList<>();
        expected.add(mockEnemyTroop);

        sut.removeTroop(mockFriendlyTroop);
        List<Troop> actual = sut.getTroops();

        assertEquals(expected, actual);
    }
}