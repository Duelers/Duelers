package test.java;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import server.dataCenter.models.card.AttackType;
import server.dataCenter.models.card.Card;
import server.dataCenter.models.card.CardType;
import server.dataCenter.models.card.spell.Spell;
import server.gameCenter.models.game.*;
import server.gameCenter.models.game.availableActions.AvailableActions;
import server.gameCenter.models.game.availableActions.Move;
import server.gameCenter.models.map.Cell;
import server.gameCenter.models.map.GameMap;

import java.util.*;

import static org.mockito.Mockito.when;

public class AvailableActions_Test {

    private Card makeMinionCard() {
        Card card = new Card(" ", " ", CardType.MINION, new ArrayList<Spell>(), 1, 1, 1, 1, AttackType.MELEE, 1);
        return card;
    }

    private Troop addFriendlyMinion(Game game, Cell cell) {
        int friendlyPlayerNumber = 1;
        Troop troop = new Troop(makeMinionCard(), cell, friendlyPlayerNumber);
        troop.setCanMove(true);
        game.getGameMap().addTroop(friendlyPlayerNumber, troop);
        game.getCurrentTurnPlayer().addTroop(troop);
        return troop;
    }

    private Troop addEnemyMinion(Game game, Cell cell) {
        int enemyPlayerNumber = 2;
        Troop troop = new Troop(makeMinionCard(), cell, enemyPlayerNumber);
        troop.setCanMove(true);
        game.getGameMap().addTroop(enemyPlayerNumber, troop);
        return troop;
    }

    @Mock
    Game mockGame;

    @Mock
    Player mockPlayer;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        GameMap map = new GameMap();
        when(mockGame.getGameMap()).thenReturn(map);
        when(mockGame.getCurrentTurnPlayer()).thenReturn(mockPlayer);

//        doCallRealMethod().when(mockPlayer).addTroop(any(Troop.class));
    }

    @Test
    public void availableMovesAtCorner_TEST() {
        Game game = mockGame;
        AvailableActions sut = new AvailableActions();

        Cell topLeft = new Cell(0, 0);
        Troop topLeftMinion = addFriendlyMinion(game, topLeft);

        when(mockPlayer.getTroops()).thenReturn(Collections.singletonList(topLeftMinion));

        sut.calculateAvailableMoves(game);

        ArrayList<Cell> expected = new ArrayList<>();
        expected.add(new Cell(0, 0));
        expected.add(new Cell(0, 1));
        expected.add(new Cell(0, 2));
        expected.add(new Cell(1, 0));
        expected.add(new Cell(1, 1));
        expected.add(new Cell(2, 0));

        List<Move> moves = sut.getMoves();
        List<Cell> actual = moves.get(0).getTargets();

        Assert.assertEquals(new HashSet<>(expected), new HashSet<>(actual));
    }

    @Test
    public void availableMovesAtEdge__TEST() {
        Game game = mockGame;
        AvailableActions sut = new AvailableActions();

        Cell centerLeft = new Cell(2, 0);
        Troop centerMinion = addFriendlyMinion(game, centerLeft);

        when(mockPlayer.getTroops()).thenReturn(Collections.singletonList(centerMinion));

        sut.calculateAvailableMoves(game);

        ArrayList<Cell> expected = new ArrayList<>();
        expected.add(new Cell(0, 0));

        expected.add(new Cell(1, 0));
        expected.add(new Cell(1, 1));

        expected.add(new Cell(2, 0));
        expected.add(new Cell(2, 1));
        expected.add(new Cell(2, 2));

        expected.add(new Cell(3, 0));
        expected.add(new Cell(3, 1));

        expected.add(new Cell(4, 0));


        List<Move> moves = sut.getMoves();
        List<Cell> actual = moves.get(0).getTargets();

        Assert.assertEquals(new HashSet<>(expected), new HashSet<>(actual));
    }

    @Test
    public void availableMovesAtCenter__TEST() {
        Game game = mockGame;
        AvailableActions sut = new AvailableActions();

        Cell center = new Cell(2, 3);
        Troop centerMinion = addFriendlyMinion(game, center);

        when(mockPlayer.getTroops()).thenReturn(Collections.singletonList(centerMinion));

        sut.calculateAvailableMoves(game);

        ArrayList<Cell> expected = new ArrayList<>();
        expected.add(new Cell(0, 3));

        expected.add(new Cell(1, 2));
        expected.add(new Cell(1, 3));
        expected.add(new Cell(1, 4));

        expected.add(new Cell(2, 1));
        expected.add(new Cell(2, 2));
        expected.add(new Cell(2, 3));
        expected.add(new Cell(2, 4));
        expected.add(new Cell(2, 5));

        expected.add(new Cell(3, 2));
        expected.add(new Cell(3, 3));
        expected.add(new Cell(3, 4));

        expected.add(new Cell(4, 3));


        List<Move> moves = sut.getMoves();
        List<Cell> actual = moves.get(0).getTargets();

        Assert.assertEquals(new HashSet<>(expected), new HashSet<>(actual));
    }

    @Test
    public void availableMovesCanNotEndOnFriendly__TEST() {
        Game game = mockGame;
        AvailableActions sut = new AvailableActions();

        Cell center = new Cell(2, 3);
        Troop centerMinion = addFriendlyMinion(game, center);

        addFriendlyMinion(game, new Cell(0, 3));
        addFriendlyMinion(game, new Cell(1, 2));
        addFriendlyMinion(game, new Cell(1, 4));
        addFriendlyMinion(game, new Cell(2, 1));
        addFriendlyMinion(game, new Cell(2, 5));
        addFriendlyMinion(game, new Cell(3, 2));
        addFriendlyMinion(game, new Cell(3, 4));
        addFriendlyMinion(game, new Cell(4, 3));

        when(mockPlayer.getTroops()).thenReturn(Collections.singletonList(centerMinion));

        sut.calculateAvailableMoves(game);

        ArrayList<Cell> expected = new ArrayList<>();
        expected.add(new Cell(1, 3));

        expected.add(new Cell(2, 2));
        expected.add(new Cell(2, 3));
        expected.add(new Cell(2, 4));

        expected.add(new Cell(3, 3));

        List<Move> moves = sut.getMoves();
        List<Cell> actual = moves.get(0).getTargets();

        Assert.assertEquals(new HashSet<>(expected), new HashSet<>(actual));
    }


    @Test
    public void availableMovesCanMoveOverFriendly__TEST() {
        Game game = mockGame;
        AvailableActions sut = new AvailableActions();

        Cell center = new Cell(2, 3);
        Troop centerMinion = addFriendlyMinion(game, center);

        addFriendlyMinion(game, new Cell(1, 3));
        addFriendlyMinion(game, new Cell(2, 2));
        addFriendlyMinion(game, new Cell(2, 4));
        addFriendlyMinion(game, new Cell(3, 3));

        when(mockPlayer.getTroops()).thenReturn(Collections.singletonList(centerMinion));

        sut.calculateAvailableMoves(game);

        ArrayList<Cell> expected = new ArrayList<>();
        expected.add(new Cell(0, 3));

        expected.add(new Cell(1, 2));
        expected.add(new Cell(1, 4));

        expected.add(new Cell(2, 1));
        expected.add(new Cell(2, 3));
        expected.add(new Cell(2, 5));

        expected.add(new Cell(3, 2));
        expected.add(new Cell(3, 4));

        expected.add(new Cell(4, 3));

        List<Move> moves = sut.getMoves();
        List<Cell> actual = moves.get(0).getTargets();

        Assert.assertEquals(new HashSet<>(expected), new HashSet<>(actual));
    }

    @Test
    public void availableMovesCannotMoveOverEnemy__TEST() {
        Game game = mockGame;
        AvailableActions sut = new AvailableActions();

        Cell center = new Cell(2, 3);
        Troop centerMinion = addFriendlyMinion(game, center);

        addEnemyMinion(game, new Cell(1, 3));
        addEnemyMinion(game, new Cell(2, 2));
        addEnemyMinion(game, new Cell(2, 4));
        addEnemyMinion(game, new Cell(3, 3));

        when(mockPlayer.getTroops()).thenReturn(Collections.singletonList(centerMinion));

        sut.calculateAvailableMoves(game);

        ArrayList<Cell> expected = new ArrayList<>();
        expected.add(new Cell(2, 3));

        List<Move> moves = sut.getMoves();
        List<Cell> actual = moves.get(0).getTargets();

        Assert.assertEquals(new HashSet<>(expected), new HashSet<>(actual));
    }

    @Test
    public void availableMovesPathAroundObstacleToAbove__TEST() {
        Game game = mockGame;
        AvailableActions sut = new AvailableActions();

        Cell center = new Cell(2, 3);
        Troop centerMinion = addFriendlyMinion(game, center);

        addEnemyMinion(game, new Cell(1, 3));

        when(mockPlayer.getTroops()).thenReturn(Collections.singletonList(centerMinion));

        sut.calculateAvailableMoves(game);

        ArrayList<Cell> expected = new ArrayList<>();

        expected.add(new Cell(1, 2));
        expected.add(new Cell(1, 4));

        expected.add(new Cell(2, 1));
        expected.add(new Cell(2, 2));
        expected.add(new Cell(2, 3));
        expected.add(new Cell(2, 4));
        expected.add(new Cell(2, 5));

        expected.add(new Cell(3, 2));
        expected.add(new Cell(3, 3));
        expected.add(new Cell(3, 4));

        expected.add(new Cell(4, 3));

        List<Move> moves = sut.getMoves();
        List<Cell> actual = moves.get(0).getTargets();

        Assert.assertEquals(new HashSet<>(expected), new HashSet<>(actual));
    }

    @Test
    public void availableMovesPathAroundObstacleToLeft__TEST() {
        Game game = mockGame;
        AvailableActions sut = new AvailableActions();

        Cell center = new Cell(2, 3);
        Troop centerMinion = addFriendlyMinion(game, center);

        addEnemyMinion(game, new Cell(2, 2));

        when(mockPlayer.getTroops()).thenReturn(Collections.singletonList(centerMinion));

        sut.calculateAvailableMoves(game);

        ArrayList<Cell> expected = new ArrayList<>();
        expected.add(new Cell(0, 3));

        expected.add(new Cell(1, 2));
        expected.add(new Cell(1, 3));
        expected.add(new Cell(1, 4));

        expected.add(new Cell(2, 3));
        expected.add(new Cell(2, 4));
        expected.add(new Cell(2, 5));

        expected.add(new Cell(3, 2));
        expected.add(new Cell(3, 3));
        expected.add(new Cell(3, 4));

        expected.add(new Cell(4, 3));

        List<Move> moves = sut.getMoves();
        List<Cell> actual = moves.get(0).getTargets();

        Assert.assertEquals(new HashSet<>(expected), new HashSet<>(actual));
    }

    @Test
    public void availableMovesPathAroundObstacleToBelow__TEST() {
        Game game = mockGame;
        AvailableActions sut = new AvailableActions();

        Cell center = new Cell(2, 3);
        Troop centerMinion = addFriendlyMinion(game, center);

        addEnemyMinion(game, new Cell(3, 3));

        when(mockPlayer.getTroops()).thenReturn(Collections.singletonList(centerMinion));

        sut.calculateAvailableMoves(game);

        ArrayList<Cell> expected = new ArrayList<>();
        expected.add(new Cell(0, 3));

        expected.add(new Cell(1, 2));
        expected.add(new Cell(1, 3));
        expected.add(new Cell(1, 4));

        expected.add(new Cell(2, 1));
        expected.add(new Cell(2, 2));
        expected.add(new Cell(2, 3));
        expected.add(new Cell(2, 4));
        expected.add(new Cell(2, 5));

        expected.add(new Cell(3, 2));
        expected.add(new Cell(3, 4));

        List<Move> moves = sut.getMoves();
        List<Cell> actual = moves.get(0).getTargets();

        Assert.assertEquals(new HashSet<>(expected), new HashSet<>(actual));
    }

    @Test
    public void availableMovesPathAroundObstacleToRight__TEST() {
        Game game = mockGame;
        AvailableActions sut = new AvailableActions();

        Cell center = new Cell(2, 3);
        Troop centerMinion = addFriendlyMinion(game, center);

        addEnemyMinion(game, new Cell(2, 4));

        when(mockPlayer.getTroops()).thenReturn(Collections.singletonList(centerMinion));

        sut.calculateAvailableMoves(game);

        ArrayList<Cell> expected = new ArrayList<>();
        expected.add(new Cell(0, 3));

        expected.add(new Cell(1, 2));
        expected.add(new Cell(1, 3));
        expected.add(new Cell(1, 4));

        expected.add(new Cell(2, 1));
        expected.add(new Cell(2, 2));
        expected.add(new Cell(2, 3));

        expected.add(new Cell(3, 2));
        expected.add(new Cell(3, 3));
        expected.add(new Cell(3, 4));

        expected.add(new Cell(4, 3));

        List<Move> moves = sut.getMoves();
        List<Cell> actual = moves.get(0).getTargets();

        Assert.assertEquals(new HashSet<>(expected), new HashSet<>(actual));
    }


}
