package view.BattleView;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.card.CardType;
import models.comperessedData.*;
import models.game.CellEffect;
import models.game.GameActions;
import models.game.map.Position;
import models.message.OnlineGame;

import java.util.ArrayList;

public class BattleSceneTester extends Application implements GameActions {
    private final int playerNumber = 1;
    private final int oppPlayerNumber = 2;
    CellEffect[] cellEffects = {
            new CellEffect(1, 2, true),
            new CellEffect(1, 3, true),
            new CellEffect(1, 4, false),
            new CellEffect(1, 5, false),

    };
    private BattleScene battleScene;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setResizable(false);
        battleScene = new BattleScene(this, generateAGame(), playerNumber, "battlemap6_middleground@2x");
        Scene scene = new Scene(battleScene.root, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private CompressedGame generateAGame() {
        final CompressedCell[][] cells = new CompressedCell[5][9];
        for (int j = 0; j < 5; j++) {
            for (int i = 0; i < 9; i++) {
                cells[j][i] = new CompressedCell(j, i, null, j + i);
            }
        }
        final CompressedPlayer player1 = new CompressedPlayer("Ali", 2, new ArrayList<>(), null,
                null, new ArrayList<>(), 1, 0, null, null);
        final CompressedPlayer player2 = new CompressedPlayer("Ali1", 1, new ArrayList<>(), null,
                null, new ArrayList<>(), 2, 0, null, null);
        final CompressedPlayer myPlayer;
        if (playerNumber == 1)
            myPlayer = player1;
        else
            myPlayer = player2;
        final ArrayList<CompressedTroop> troops = new ArrayList<>();
        final CompressedGameMap map = new CompressedGameMap(cells, troops);
        final CompressedGame game = new CompressedGame(player1, player2, map, 7, null);

        new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            CompressedCard card = new CompressedCard("generalspell_f5_overload", null, "a1", CardType.SPELL,
                    null, 0, 0, 0, null, 2, true);
            myPlayer.addCardToNext(card);
            game.gameUpdate(10, 1, 0, 2, 0, cellEffects);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            myPlayer.addNextCardToHand();
            myPlayer.removeCardFromNext();
            game.gameUpdate(11, 4, 0, 4, 0, cellEffects);
            card = new CompressedCard("boss_christmas", null, "a1", CardType.MINION,
                    null, 0, 0, 0, null, 2, true);
            CompressedTroop troop = new CompressedTroop(card, 5, 6, 5, new Position(2, 2),
                    true, true, false, false, 1, 1);
            myPlayer.addCardToNext(card);
            map.updateTroop(troop);
            String s = "boss_wolfpunch";
            card = new CompressedCard(s, null, "a3", CardType.HERO,
                    new CompressedSpell("a", null, null, 0, 2, 3),
                    0, 0, 0, null, 2, true);
            troop = new CompressedTroop(card, 5, 6, 5, new Position(1, 3),
                    true, true, false, false, 1, 1);
            map.updateTroop(troop);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            myPlayer.addNextCardToHand();
            card = new CompressedCard(s, null, "a2", CardType.MINION,
                    null, 0, 0, 0, null, 2, true);
            troop = new CompressedTroop(card, 5, 6, 5, new Position(4, 4),
                    true, true, false, false, 1, 2);
            map.updateTroop(troop);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            troop = new CompressedTroop(card, 5, 6, 5, new Position(4, 8),
                    true, true, false, false, 1, 2);
            map.updateTroop(troop);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            troop = new CompressedTroop(card, 5, 6, 5, new Position(1, 4),
                    true, true, false, false, 1, 2);
            map.updateTroop(troop);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            battleScene.spell("fx_f1_aurynnexus", new Position(1, 1));
        }).start();
        return game;
    }

    @Override
    public void attack(CompressedTroop selectedTroop, CompressedTroop troop) {
        battleScene.attack(selectedTroop.getCard().getCardId(), troop.getCard().getCardId());
        battleScene.defend(troop.getCard().getCardId(), selectedTroop.getCard().getCardId());
        battleScene.attack(troop.getCard().getCardId(), selectedTroop.getCard().getCardId());
    }

    @Override
    public void comboAttack(ArrayList<CompressedTroop> comboTroops, CompressedTroop troop) {
        for (CompressedTroop comboAttacker : comboTroops) {
            battleScene.attack(comboAttacker.getCard().getCardId(), troop.getCard().getCardId());
        }
        battleScene.defend(troop.getCard().getCardId(), comboTroops.get(comboTroops.size() - 1).getCard().getCardId());
        battleScene.attack(troop.getCard().getCardId(), comboTroops.get(comboTroops.size() - 1).getCard().getCardId());
    }

    @Override
    public void move(CompressedTroop selectedTroop, int j, int i) {
        battleScene.getMapBox().getGameMap().updateTroop(new CompressedTroop(selectedTroop, j, i));
    }

    @Override
    public void endTurn() {
        battleScene.getGame().gameUpdate(battleScene.getGame().getTurnNumber() + 1, 3,
                0, 3, 0, cellEffects);
        System.out.println("end turn");
        System.out.println("new turn:" + battleScene.getGame().getTurnNumber());
    }

    @Override
    public void insert(CompressedCard compressedCard, int row, int column) {
        System.out.println("insert");
    }

    @Override
    public void useSpecialPower(int row, int column) {
        System.out.println("use spell");
    }

    @Override
    public void exitGameShow(OnlineGame onlineGame) {

    }

    @Override
    public void forceFinish() {

    }

    @Override
    public void setNewNextCard(){

    }

    @Override
    public void replaceCard(String cardID) {

    }
}
