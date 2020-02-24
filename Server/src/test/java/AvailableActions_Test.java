package test.java;


import org.junit.Assert;
import org.junit.Test;
import server.dataCenter.models.account.Account;
import server.dataCenter.models.card.AttackType;
import server.dataCenter.models.card.Card;
import server.dataCenter.models.card.CardType;
import server.dataCenter.models.card.spell.Spell;
import server.gameCenter.models.game.Game;
import server.gameCenter.models.game.KillHeroBattle;
import server.gameCenter.models.game.Troop;
import server.gameCenter.models.game.availableActions.AvailableActions;
import server.gameCenter.models.game.availableActions.Move;
import server.gameCenter.models.map.Cell;
import server.gameCenter.models.map.GameMap;

import java.util.ArrayList;
import java.util.List;

public class AvailableActions_Test {

    private Game makeGame() {
        Account fakeAccount0 = new Account("0", "");
        Account fakeAccount1 = new Account("1", "");
        GameMap gameMap = new GameMap();
        Game game = new KillHeroBattle(fakeAccount0, fakeAccount1, gameMap);
        return game;
    }

    private Card makeMinionCard(){
        Card card = new Card(" ", " ", CardType.MINION, new ArrayList<Spell>(), 1, 1, 1, 1, AttackType.MELEE, 1);
        return card;
    }

    private Troop addFriendlyMinion(Game game, Cell cell){
        int friendlyPlayerNumber = 1;
        Troop troop = new Troop(makeMinionCard(), cell, friendlyPlayerNumber);
        game.getGameMap().addTroop(friendlyPlayerNumber, troop);
        return troop;
    }


    @Test
    public void availableMoves_WIPTEST() {
        Game game = makeGame();
        AvailableActions sut = new AvailableActions();

        Cell topLeft = new Cell(0, 0);
        Troop topLeftMinion = addFriendlyMinion(game, topLeft);

        sut.calculateAvailableMoves(game);

        sut.getMoves();

        ArrayList<Move> expected = new ArrayList<>();
        ArrayList<Cell> topLeftTargets = new ArrayList<>();
        topLeftTargets.add(new Cell(0, 0));
        topLeftTargets.add(new Cell(0, 1));
        topLeftTargets.add(new Cell(0, 2));
        topLeftTargets.add(new Cell(1, 0));
        topLeftTargets.add(new Cell(1, 1));
        topLeftTargets.add(new Cell(2, 0));
        expected.add(new Move(topLeftMinion, topLeftTargets));

        List<Move> actual = sut.getMoves();

        Assert.assertEquals(expected, actual);
    }

}
