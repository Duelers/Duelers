package test.java;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import shared.models.card.AttackType;
import shared.models.card.Card;
import shared.models.card.CardType;
import shared.models.card.spell.Spell;
import server.gameCenter.models.game.*;
import server.gameCenter.models.game.availableActions.AvailableActions;
import server.gameCenter.models.game.availableActions.Move;
import shared.models.game.map.Cell;
import server.gameCenter.models.map.GameMap;

import java.util.*;

import static org.mockito.Mockito.when;

public class AvailableActions_Test {

    private Card makeMinionCard() {
        return new Card(" ", " ", " ", " ", CardType.MINION, new ArrayList<>(), 1, 1, 1, 1, AttackType.MELEE, 1);
    }

    private ServerTroop addFriendlyMinion(Game game, Cell cell) {
        int friendlyPlayerNumber = 1;
        ServerTroop troop = new ServerTroop(makeMinionCard(), friendlyPlayerNumber);
        troop.setCell(cell);
        troop.setCanMove(true);
        game.getGameMap().addTroop(friendlyPlayerNumber, troop);
        game.getCurrentTurnPlayer().addTroop(troop);
        return troop;
    }

    private ServerTroop addEnemyMinion(Game game, Cell cell) {
        int enemyPlayerNumber = 2;
        ServerTroop troop = new ServerTroop(makeMinionCard(), enemyPlayerNumber);
        troop.setCell(cell);
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

        when(mockPlayer.getPlayerNumber()).thenReturn(1);

        GameMap map = new GameMap();
        when(mockGame.getGameMap()).thenReturn(map);
        when(mockGame.getCurrentTurnPlayer()).thenReturn(mockPlayer);

//        doCallRealMethod().when(mockPlayer).addTroop(any(ServerTroop.class));
    }

    @Test
    public void availableMovesAtCorner_TEST() {
        Game game = mockGame;
        AvailableActions sut = new AvailableActions();

        Cell topLeft = new Cell(0, 0);
        ServerTroop topLeftMinion = addFriendlyMinion(game, topLeft);

        when(mockPlayer.getTroops()).thenReturn(Collections.singletonList(topLeftMinion));

        sut.calculateAvailableMoves(game);

        ArrayList<Cell> expected = new ArrayList<>();
//        expected.add(new Cell(0, 0));
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
        ServerTroop centerMinion = addFriendlyMinion(game, centerLeft);

        when(mockPlayer.getTroops()).thenReturn(Collections.singletonList(centerMinion));

        sut.calculateAvailableMoves(game);

        ArrayList<Cell> expected = new ArrayList<>();
        expected.add(new Cell(0, 0));

        expected.add(new Cell(1, 0));
        expected.add(new Cell(1, 1));

//        expected.add(new Cell(2, 0));
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
        ServerTroop centerMinion = addFriendlyMinion(game, center);

        when(mockPlayer.getTroops()).thenReturn(Collections.singletonList(centerMinion));

        sut.calculateAvailableMoves(game);

        ArrayList<Cell> expected = new ArrayList<>();
        expected.add(new Cell(0, 3));

        expected.add(new Cell(1, 2));
        expected.add(new Cell(1, 3));
        expected.add(new Cell(1, 4));

        expected.add(new Cell(2, 1));
        expected.add(new Cell(2, 2));
//        expected.add(new Cell(2, 3));
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
        ServerTroop centerMinion = addFriendlyMinion(game, center);

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
//        expected.add(new Cell(2, 3));
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
        ServerTroop centerMinion = addFriendlyMinion(game, center);

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
//        expected.add(new Cell(2, 3));
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
        ServerTroop centerMinion = addFriendlyMinion(game, center);

        addEnemyMinion(game, new Cell(1, 3));
        addEnemyMinion(game, new Cell(2, 2));
        addEnemyMinion(game, new Cell(2, 4));
        addEnemyMinion(game, new Cell(3, 3));

        when(mockPlayer.getTroops()).thenReturn(Collections.singletonList(centerMinion));

        sut.calculateAvailableMoves(game);

//        ArrayList<Cell> expected = new ArrayList<>();
//        expected.add(new Cell(2, 3));

        int expected = 0;

        List<Move> moves = sut.getMoves();
//        List<Cell> actual = moves.get(0).getTargets();
        int actual = moves.size();


        Assert.assertEquals(expected, actual);
    }

    @Test
    public void availableMovesPathAroundObstacleToAbove__TEST() {
        Game game = mockGame;
        AvailableActions sut = new AvailableActions();

        Cell center = new Cell(2, 3);
        ServerTroop centerMinion = addFriendlyMinion(game, center);

        addEnemyMinion(game, new Cell(1, 3));

        when(mockPlayer.getTroops()).thenReturn(Collections.singletonList(centerMinion));

        sut.calculateAvailableMoves(game);

        ArrayList<Cell> expected = new ArrayList<>();

        expected.add(new Cell(1, 2));
        expected.add(new Cell(1, 4));

        expected.add(new Cell(2, 1));
        expected.add(new Cell(2, 2));
//        expected.add(new Cell(2, 3));
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
        ServerTroop centerMinion = addFriendlyMinion(game, center);

        addEnemyMinion(game, new Cell(2, 2));

        when(mockPlayer.getTroops()).thenReturn(Collections.singletonList(centerMinion));

        sut.calculateAvailableMoves(game);

        ArrayList<Cell> expected = new ArrayList<>();
        expected.add(new Cell(0, 3));

        expected.add(new Cell(1, 2));
        expected.add(new Cell(1, 3));
        expected.add(new Cell(1, 4));

//        expected.add(new Cell(2, 3));
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
        ServerTroop centerMinion = addFriendlyMinion(game, center);

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
//        expected.add(new Cell(2, 3));
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
        ServerTroop centerMinion = addFriendlyMinion(game, center);

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
//        expected.add(new Cell(2, 3));

        expected.add(new Cell(3, 2));
        expected.add(new Cell(3, 3));
        expected.add(new Cell(3, 4));

        expected.add(new Cell(4, 3));

        List<Move> moves = sut.getMoves();
        List<Cell> actual = moves.get(0).getTargets();

        Assert.assertEquals(new HashSet<>(expected), new HashSet<>(actual));
    }


}
