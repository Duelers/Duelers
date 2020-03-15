package test.java;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import shared.models.card.Card;
import shared.models.game.BasePlayer;
import shared.models.game.Troop;
import shared.models.game.map.Cell;

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

    @Mock
    Card mockTroopCard;

    String testId = "testId";

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        when(mockTroop.getCell()).thenReturn(new Cell(0, 0));
        when(mockTroop.getCard()).thenReturn(mockTroopCard);

        when(mockTroopCard.getCardId()).thenReturn(testId);
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

    @Test
    public void getTroop_noTroopWithGivenId_returnsNull() {
        BasePlayer<Card, Troop> sut = makeBasePlayer();
        Troop troopInCell = sut.getTroop("someBadId");

        assertNull(troopInCell);
    }


    @Test
    public void getTroop_troopWithGivenId_returnsTroop() {
        BasePlayer<Card, Troop> sut = makeBasePlayer();
        Troop actual = sut.getTroop(testId);
        Troop expected = mockTroop;

        assertEquals(expected, actual);
    }

}