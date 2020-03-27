package test.java.models.game;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import shared.models.card.Card;
import shared.models.game.BaseGame;
import shared.models.game.BasePlayer;
import shared.models.game.GameType;
import shared.models.game.Troop;
import shared.models.game.map.BaseGameMap;
import shared.models.game.map.Cell;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class BaseGame_Test {


    @Mock
    BasePlayer<Card, Troop> mockPlayerOne;

    @Mock
    BasePlayer<Card, Troop> mockPlayerTwo;

    @Mock
    BaseGameMap<Troop> mockGameMap;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        when(mockPlayerOne.getPlayerNumber()).thenReturn(1);
        when(mockPlayerTwo.getPlayerNumber()).thenReturn(2);
    }

    @Test
    public void getCurrentTurnPlayer_whenTurnOne_playerOne() {
        BaseGame<BasePlayer<Card, Troop>, BaseGameMap<Troop>> sut = new BaseGame<>(
                mockPlayerOne, mockPlayerTwo, mockGameMap, 1, GameType.KILL_HERO
        );
        int expected = 1;
        int actual = sut.getCurrentTurnPlayer().getPlayerNumber();
        assertEquals(expected, actual);
    }

    @Test
    public void getCurrentTurnPlayer_whenTurnEleven_playerOne() {
        BaseGame<BasePlayer<Card, Troop>, BaseGameMap<Troop>> sut = new BaseGame<>(
                mockPlayerOne, mockPlayerTwo, mockGameMap, 11, GameType.KILL_HERO
        );
        int expected = 1;
        int actual = sut.getCurrentTurnPlayer().getPlayerNumber();
        assertEquals(expected, actual);
    }

    @Test
    public void getCurrentTurnPlayer_whenTurnTwo_playerTwo() {
        BaseGame<BasePlayer<Card, Troop>, BaseGameMap<Troop>> sut = new BaseGame<>(
                mockPlayerOne, mockPlayerTwo, mockGameMap, 2, GameType.KILL_HERO
        );
        int expected = 2;
        int actual = sut.getCurrentTurnPlayer().getPlayerNumber();
        assertEquals(expected, actual);
    }

    @Test
    public void getCurrentTurnPlayer_whenTurnTwelve_playerTwo() {
        BaseGame<BasePlayer<Card, Troop>, BaseGameMap<Troop>> sut = new BaseGame<>(
                mockPlayerOne, mockPlayerTwo, mockGameMap, 12, GameType.KILL_HERO
        );
        int expected = 2;
        int actual = sut.getCurrentTurnPlayer().getPlayerNumber();
        assertEquals(expected, actual);
    }

}
