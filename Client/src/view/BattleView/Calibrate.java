package view.BattleView;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.card.CardType;
import models.card.spell.AvailabilityType;
import models.comperessedData.*;
import models.game.GameActions;
import models.game.map.Position;
import models.message.OnlineGame;

import java.util.ArrayList;

public class Calibrate extends Application implements GameActions {
    private final int playerNumber = 1;
    private BattleScene battleScene;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setResizable(false);
        battleScene = new BattleScene(this, calibrateGame(), playerNumber, "battlemap6_middleground@2x");
        battleScene.getMapBox().addCircles();
        Scene scene = new Scene(battleScene.root, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.show();
        new Thread(() -> {
            while (true) {
                Platform.runLater(() -> {
                    battleScene.spell(new AvailabilityType(false, false, false, false, false
                            , false, true), new Position(0, 0));//TODO
                });
                try {
                    Thread.sleep(6000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private CompressedGame calibrateGame() {
        final CompressedCell[][] cells = new CompressedCell[5][9];
        for (int j = 0; j < 5; j++) {
            for (int i = 0; i < 9; i++) {
                cells[j][i] = new CompressedCell(j, i, null, 0);
            }
        }
        final CompressedPlayer player1 = new CompressedPlayer("Ali", 2, new ArrayList<>(), new ArrayList<>(),
                null, new ArrayList<>(), 1, 0, new ArrayList<>(), null);
        final CompressedPlayer player2 = new CompressedPlayer("Ali1", 1, new ArrayList<>(), new ArrayList<>(),
                null, new ArrayList<>(), 2, 0, new ArrayList<>(), null);
        final CompressedPlayer myPlayer;
        if (playerNumber == 1)
            myPlayer = player1;
        else
            myPlayer = player2;
        final ArrayList<CompressedTroop> troops = new ArrayList<>();
        final CompressedGameMap map = new CompressedGameMap(cells, troops);
        final CompressedGame game = new CompressedGame(player1, player2, map, 7, null);

        new Thread(() -> {
            CompressedCard card = new CompressedCard("boss_harmony", null, "a1", CardType.MINION,
                    null, 0, 0, 0, null, 2, true);
            CompressedTroop troop = new CompressedTroop(card, 5, 6, 5, new Position(0, 0),
                    true, true, false, false, 1, 1);
            map.updateTroop(troop);
            player1.addCardToNext(card);
            player1.addNextCardToHand();
            player1.removeCardFromNext();
            player1.addCardToNext(card);

        }).start();
        return game;
    }

    @Override
    public void attack(CompressedTroop selectedTroop, CompressedTroop troop) {

    }

    @Override
    public void comboAttack(ArrayList<CompressedTroop> comboTroops, CompressedTroop troop) {

    }

    @Override
    public void move(CompressedTroop selectedTroop, int j, int i) {

    }

    @Override
    public void endTurn() {

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
    public void setNewNextCard() {

    }

    @Override
    public void replaceCard(String cardID) {

    }

    @Override
    public void forceFinish() {

    }
}
