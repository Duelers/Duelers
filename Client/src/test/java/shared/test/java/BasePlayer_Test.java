package shared.test.java;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import server.gameCenter.models.map.GameMap;
import shared.models.card.Card;
import shared.models.game.BasePlayer;
import shared.models.game.Troop;
import shared.models.game.map.Cell;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class BasePlayer_Test {

    BasePlayer<Card, Troop> makeBasePlayer() {
        ArrayList<Troop> troops = new ArrayList<>();
        troops.add(mockTroop);
        return new BasePlayer<>("userName", 0,
                new ArrayList<>(), new ArrayList<>(), null, 1, troops, null);
    }

    @Mock
    Troop mockTroop;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        when(mockTroop.getCell()).thenReturn(new Cell(0, 0));
    }

    @Test
    public void getTroop_noTroopWithGivenCell_returnsNull() {
        BasePlayer<Card, Troop> sut = makeBasePlayer();
        Cell targetCell = new Cell(1, 1);
        Troop troopInCell = sut.getTroop(targetCell);

        assertNull(troopInCell);
    }


    @Test
    public void getTroop_troopWithGivenCell_returnsTroop() {
        BasePlayer<Card, Troop> sut = makeBasePlayer();
        Cell targetCell = new Cell(0, 0);
        Troop actual = sut.getTroop(targetCell);
        Troop expected = mockTroop;

        assertEquals(expected, actual);
    }

}