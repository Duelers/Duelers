package org.projectcardboard.client.view.battleview;

import static org.projectcardboard.client.view.battleview.Constants.NEGATIVE_BUFF_EFFECT;
import static org.projectcardboard.client.view.battleview.Constants.POSITIVE_BUFF_EFFECT;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;

import org.projectcardboard.client.controller.GameController;
import org.projectcardboard.client.controller.SoundEffectPlayer;
import org.projectcardboard.client.models.compresseddata.CompressedGameMap;
import org.projectcardboard.client.models.game.Player;
import org.projectcardboard.client.models.gui.CardPane;

import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import shared.models.card.Card;
import shared.models.card.CardType;
import shared.models.game.Troop;
import shared.models.game.map.Cell;

public class MapBox implements PropertyChangeListener {
    private final BattleScene battleScene;
    private final CompressedGameMap gameMap;
    private final Group mapGroup;
    private final Polygon[][] cells = new Polygon[5][9];
    private final double[][] cellsX = new double[5][9];
    private final double[][] cellsY = new double[5][9];
    private final HashMap<Troop, TroopAnimation> troopAnimationHashMap = new HashMap<>();
    private Troop selectedTroop = null;
    private boolean spellSelected = false;
    private CardPane cardPane = null;
    private SelectionType selectionType;


    MapBox(BattleScene battleScene, CompressedGameMap gameMap) {
        this.battleScene = battleScene;
        this.gameMap = gameMap;
        mapGroup = new Group();
        mapGroup.setLayoutY(Constants.MAP_Y);
        mapGroup.setLayoutX(Constants.MAP_X);
        makePolygons();
        resetSelection();
        for (Troop troop : gameMap.getTroops()) {
            updateTroop(null, troop);
        }
        gameMap.addPropertyChangeListener(this);
    }

    private void makePolygons() {
        for (int j = 0; j < CompressedGameMap.getRowNumber(); j++) {
            double upperWidth = (j * Constants.MAP_DOWNER_WIDTH + (6 - j) * Constants.MAP_UPPER_WIDTH) / 6;
            double downerWidth = ((j + 1) * Constants.MAP_DOWNER_WIDTH + (6 - (j + 1)) * Constants.MAP_UPPER_WIDTH) / 6;
            double upperY = Constants.MAP_HEIGHT * (upperWidth - Constants.MAP_UPPER_WIDTH) /
                    (Constants.MAP_DOWNER_WIDTH - Constants.MAP_UPPER_WIDTH);
            double downerY = Constants.MAP_HEIGHT * (downerWidth - Constants.MAP_UPPER_WIDTH) /
                    (Constants.MAP_DOWNER_WIDTH - Constants.MAP_UPPER_WIDTH);
            for (int i = 0; i < CompressedGameMap.getColumnNumber(); i++) {
                double x1 = (Constants.MAP_DOWNER_WIDTH - upperWidth) / 2 + i * upperWidth / 9;
                double x2 = (Constants.MAP_DOWNER_WIDTH - upperWidth) / 2 + (i + 1) * upperWidth / 9;
                double x3 = (Constants.MAP_DOWNER_WIDTH - downerWidth) / 2 + (i + 1) * downerWidth / 9;
                double x4 = (Constants.MAP_DOWNER_WIDTH - downerWidth) / 2 + i * downerWidth / 9;
                cells[j][i] = new Polygon(x1 + Constants.SPACE_BETWEEN_CELLS / 2,
                        upperY + Constants.SPACE_BETWEEN_CELLS / 2, x2 - Constants.SPACE_BETWEEN_CELLS / 2,
                        upperY + Constants.SPACE_BETWEEN_CELLS / 2, x3 - Constants.SPACE_BETWEEN_CELLS / 2,
                        downerY - Constants.SPACE_BETWEEN_CELLS / 2, x4 + Constants.SPACE_BETWEEN_CELLS / 2,
                        downerY - Constants.SPACE_BETWEEN_CELLS / 2);
                cells[j][i].setFill(Color.DARKBLUE);
                cells[j][i].setOpacity(Constants.CELLS_DEFAULT_OPACITY);
                final int I = i, J = j;
                cells[j][i].setOnMouseEntered(mouseEvent -> hoverCell(J, I));
                cells[j][i].setOnMouseExited(mouseEvent -> exitCell(J, I));
                cells[j][i].setOnMouseClicked(mouseEvent -> clickCell(J, I));
                mapGroup.getChildren().add(cells[j][i]);
                cellsX[j][i] = (x1 + x2 + x3 + x4) / 4;
                cellsY[j][i] = (upperY + downerY) / 2;
            }
        }
    }

    void addCircles() {
        for (int j = 0; j < CompressedGameMap.getRowNumber(); j++) {
            for (int i = 0; i < CompressedGameMap.getColumnNumber(); i++) {
                mapGroup.getChildren().add(new Circle(cellsX[j][i], cellsY[j][i], 2));
            }
        }
    }

    private void updateTroop(Troop oldTroop, Troop newTroop) {
        final TroopAnimation animation;
        if (newTroop == null) {
            animation = troopAnimationHashMap.get(oldTroop);
            if (animation != null) {
                animation.updateApHp(oldTroop.getCurrentAp(), 0);
                animation.kill();
                troopAnimationHashMap.remove(oldTroop);
            }
        } else if (oldTroop != null && troopAnimationHashMap.containsKey(oldTroop)) {
            animation = troopAnimationHashMap.get(oldTroop);
            troopAnimationHashMap.remove(oldTroop);
            troopAnimationHashMap.put(newTroop, animation);
            animation.updateApHp(newTroop.getCurrentAp(), newTroop.getCurrentHp());

            Cell oldCell = oldTroop.getCell();
            Cell newCell = newTroop.getCell();

            if (!newCell.equals(oldCell))
                animation.moveTo(newCell.getRow(), newCell.getColumn());
        } else {
            try {
                animation = new TroopAnimation(mapGroup, cellsX, cellsY, newTroop.getCard().getSpriteName(),
                        newTroop.getCell().getRow(), newTroop.getCell().getColumn(),
                        newTroop.getPlayerNumber() == 1,
                        newTroop.getPlayerNumber() == battleScene.getMyPlayerNumber());
                animation.updateApHp(newTroop.getCurrentAp(), newTroop.getCurrentHp());
                animation.getTroopGroup().setOnMouseClicked(mouseEvent -> clickCell(animation.getRow(), animation.getColumn()));
                animation.getTroopGroup().setOnMouseEntered(mouseEvent -> hoverCell(animation.getRow(), animation.getColumn()));
                animation.getTroopGroup().setOnMouseExited(mouseEvent -> exitCell(animation.getRow(), animation.getColumn()));
                troopAnimationHashMap.put(newTroop, animation);
            } catch (Exception e) {
                System.out.println("Error making animation " + newTroop.getCard().getCardId());
            }
        }
        battleScene.getHandBox().resetSelection();
        resetSelection();
    }

    Group getMapGroup() {
        return mapGroup;
    }

    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("troop")) {
            Platform.runLater(() -> updateTroop((Troop) evt.getOldValue(), (Troop) evt.getNewValue()));
        }
        if (evt.getPropertyName().equals("cellEffect")) {
            Platform.runLater(this::updateCellEffects);
        }
    }

    void resetSelection() {
        selectedTroop = null;
        spellSelected = false;
        for (TroopAnimation animation : troopAnimationHashMap.values()) {
            animation.diSelect();
        }
        updateMapColors();
    }

    private void exitCell(int j, int i) {
        cells[j][i].setOpacity(Constants.CELLS_DEFAULT_OPACITY);
        if (cardPane != null) {
            mapGroup.getChildren().remove(cardPane);
            cardPane = null;
        }
        Troop troop = getTroop(j, i);
        if (troop == null)
            return;
        TroopAnimation animation = troopAnimationHashMap.get(troop);
        if (animation == null)
            return;
        if (!(troop.equals(selectedTroop))) {
            animation.diSelect();
        }
    }

    private void hoverCell(int row, int column) {
        Troop troop = getTroop(row, column);
        if (troop != null) {
            TroopAnimation animation = troopAnimationHashMap.get(troop);
            animation.select();
            if (cardPane != null) {
                mapGroup.getChildren().remove(cardPane);
                cardPane = null;
            }
            try {
                cardPane = new CardPane(troop.getCard(), false, false, null);
                cardPane.setLayoutY(Constants.MAP_HEIGHT / 3);
                if (troop.getPlayerNumber() == 1)
                    cardPane.setLayoutX(-cardPane.getWidth() - 250 * Constants.SCALE);
                else
                    cardPane.setLayoutX(Constants.MAP_DOWNER_WIDTH + cardPane.getWidth());
                mapGroup.getChildren().add(cardPane);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        cells[row][column].setOpacity(Constants.CELLS_DEFAULT_OPACITY * 1.5);
    }

    private void clickCell(int row, int column) {
        if (!battleScene.isMyTurn()) {
            return;
        }
        Player player = GameController.getInstance().getCurrentGame().getCurrentTurnPlayer();
        Troop currentTroop = getTroop(row, column);
        if (selectionType.equals(SelectionType.INSERTION)) {
            if (GameController.getInstance().getAvailableActions().canInsertCard(battleScene.getHandBox().getSelectedCard())) {

                Card card = battleScene.getHandBox().getSelectedCard();
                if (card.getType().equals(CardType.MINION) || card.getType().equals(CardType.HERO)) {
                    if (GameController.getInstance().getAvailableActions().canDeployMinionOnSquare(gameMap, player, card, row, column)) {
                        battleScene.getController().insert(card, row, column);
                        System.out.println("Insert " + battleScene.getHandBox().getSelectedCard().getCardId());
                        battleScene.getHandBox().resetSelection();
                        resetSelection();
                    }
                } else if (card.getType().equals(CardType.SPELL)) {
                    battleScene.getController().insert(card, row, column);
                    System.out.println("Insert " + battleScene.getHandBox().getSelectedCard().getCardId());
                    battleScene.getHandBox().resetSelection();
                    resetSelection();
                }
            }
            return;
        }
        if (selectionType.equals(SelectionType.SELECTION)) {
            if (currentTroop != null && currentTroop.getPlayerNumber() == battleScene.getMyPlayerNumber()) {
                selectedTroop = currentTroop;
                updateMapColors();
                SoundEffectPlayer.getInstance().playSound(SoundEffectPlayer.SoundName.select);
                System.out.println("Select " + currentTroop.getCard().getCardId());
            }
            return;
        }
        if (selectedTroop != null && selectedTroop.getCell().getRow() == row &&
                selectedTroop.getCell().getColumn() == column) {
            System.out.println("DiSelect");
            SoundEffectPlayer.getInstance().playSound(SoundEffectPlayer.SoundName.select);
            battleScene.getHandBox().resetSelection();
            resetSelection();
            return;
        }
        if (selectionType.equals(SelectionType.NORMAL)) {
            if (GameController.getInstance().getAvailableActions().canAttack(gameMap, player,
                    selectedTroop, row, column)) {
                battleScene.getController().attack(selectedTroop, currentTroop);
                System.out.println(selectedTroop + " attacked to " + currentTroop);
                battleScene.getHandBox().resetSelection();
                resetSelection();
                SoundEffectPlayer.getInstance().playSound(SoundEffectPlayer.SoundName.attack);
            } else if (GameController.getInstance().getAvailableActions().canMove(gameMap, player,
                    selectedTroop, row, column)) {
                battleScene.getController().move(selectedTroop, row, column);
                System.out.println(selectedTroop.getCard().getCardId() + " moved");
                battleScene.getHandBox().resetSelection();
                resetSelection();
                SoundEffectPlayer.getInstance().playSound(SoundEffectPlayer.SoundName.move);
            }
        }
    }

    void updateMapColors() {
        updateSelectionType();
        Player player = GameController.getInstance().getCurrentGame().getCurrentTurnPlayer();
        for (int row = 0; row < CompressedGameMap.getRowNumber(); row++) {
            for (int column = 0; column < CompressedGameMap.getColumnNumber(); column++) {

                cells[row][column].setFill(Constants.defaultColor);

                if (selectionType.equals(SelectionType.INSERTION)) {
                    updateMapColoursOnInsertion(row, column, player);
                }
                if (selectionType.equals(SelectionType.NORMAL)){
                    boolean updateNormal = updateMapColoursOnNormal(row, column, player);
                    if (updateNormal) { continue;}
                }

                updateMapColourHighlightEnemyUnits(row, column, player);
                updateMapColoursHighlightUnitActions(row, column, player);

                if (selectedTroop != null && selectedTroop.getCell().equals(new Cell(row, column))){
                    cells[row][column].setFill(Constants.SELECTED_COLOR);
                }
            }
        }
    }

    private void updateMapColoursHighlightUnitActions(int row, int column, Player player){
        Troop troop = getTroop(row, column);
        if (troop == null){ return; }
        if (!battleScene.isMyTurn()) { return; }
        if (troop.getPlayerNumber() != player.getPlayerNumber()) { return;}

        if (troop.canAttack()){
            cells[row][column].setFill(Constants.CAN_ATTACK);
        }

        if (troop.canMove()){
            cells[row][column].setFill(Constants.CAN_MOVE);
        }
    }

    private void updateMapColourHighlightEnemyUnits(int row, int column, Player player){
        Troop troop = getTroop(row, column);
        if (troop == null){ return; }

        if (troop.getPlayerNumber() != this.battleScene.getMyPlayerNumber()) {
            cells[row][column].setFill(Constants.ENEMY_UNIT);
        }

    }

    private Boolean updateMapColoursOnInsertion(int row, int column, Player player){

        Card card = battleScene.getHandBox().getSelectedCard();

        if (GameController.getInstance().getAvailableActions().canInsertCard(card)) {

            if (card.getType().equals(CardType.SPELL)) {
                cells[row][column].setFill(Constants.SPELL_COLOR);
            } else { // MINION or HERO
                if (GameController.getInstance().getAvailableActions().canDeployMinionOnSquare(gameMap, player, card, row, column)) {
                    cells[row][column].setFill(Constants.DEPLOY_TROOP);
                }
            }
            return true; // has/has not updated
        }
        return false;
    }

    private Boolean updateMapColoursOnNormal(int row, int column, Player player){

        boolean canAttack = GameController.getInstance().getAvailableActions().canAttack(gameMap, player, selectedTroop, row, column);
        boolean canMove = GameController.getInstance().getAvailableActions().canMove(gameMap, player, selectedTroop, row, column);

        if (canAttack) {
            cells[row][column].setFill(Constants.ATTACK_COLOR);
        }
        if (canMove) {
            cells[row][column].setFill(Constants.MOVE_COLOR);
        }
        boolean hasUpdated = canAttack || canMove;
        return hasUpdated;
    }

    private void updateCellEffects() {
        for (int row = 0; row < 5; row++) {
            for (int column = 0; column < 9; column++) {
                int effect = gameMap.getCellEffect(row, column);
                if (effect > 0) {
                    cells[row][column].setEffect(POSITIVE_BUFF_EFFECT);
                } else if (effect < 0) {
                    cells[row][column].setEffect(NEGATIVE_BUFF_EFFECT);
                } else {
                    cells[row][column].setEffect(null);
                }
            }
        }
    }

    private void updateSelectionType() {
        if (battleScene.getHandBox().getSelectedCard() != null) {
            selectionType = SelectionType.INSERTION;
            return;
        }
        if (selectedTroop == null) {
            selectionType = SelectionType.SELECTION;
            return;
        }
        if (isSpellSelected()) {
            selectionType = SelectionType.SPELL;
            return;
        }
        selectionType = SelectionType.NORMAL;
    }

    private Troop getTroop(int j, int i) {
        for (Troop troop : troopAnimationHashMap.keySet()) {
            if (troop.getCell().getRow() == j && troop.getCell().getColumn() == i)
                return troop;
        }
        return null;
    }

    Troop getSelectedTroop() {
        return selectedTroop;
    }

    void setSpellSelected() {
        this.spellSelected = true;
    }

    boolean isSpellSelected() {
        return spellSelected;
    }

    CompressedGameMap getGameMap() {
        return gameMap;
    }

    void showAttack(String cardId, String defender) {
        if (cardId == null) {
            System.out.println("MapBox attack cardID is null");
        } else if (defender == null) {
            System.out.println("MapBox defender is null");
        }
        Troop troop = gameMap.getTroop(cardId);
        Troop defenderTroop = gameMap.getTroop(defender);
        if (troop == null) {
            System.out.println("MapBox Error attacking troop is null");
        } else if (defenderTroop == null) {
            System.out.println("MapBox Error troop being attacked is null");
        } else {
            TroopAnimation animation = troopAnimationHashMap.get(troop);
            if (animation == null)
                System.out.println("MapBox attack animation is null");
            else
                animation.attack(defenderTroop.getCell().getColumn());
        }
    }

    void showDefend(String defender, String attacker) {
        Troop troop = gameMap.getTroop(defender);
        Troop attackerTroop = gameMap.getTroop(attacker);
        if (troop == null){
            System.out.println("MapBox Error showDefend defending troop is null");
        }
        else if (attackerTroop == null){
            System.out.println("MapBox Error showDefend attacking troop is null");
        }

        else {
            TroopAnimation animation = troopAnimationHashMap.get(troop);
            if (animation == null)
                System.out.println("MapBox Error defending animation is null");
            else
                animation.hit(attackerTroop.getCell().getColumn());
        }
    }

    void showSpell(String spriteName, int j, int i) {
        Platform.runLater(() -> {
            try {
                new SpellAnimation(mapGroup, spriteName, cellsX[j][i], cellsY[j][i]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    enum SelectionType {
        INSERTION, SELECTION, SPELL, NORMAL
    }
}
