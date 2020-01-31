package view.BattleView;

import controller.GameController;
import controller.SoundEffectPlayer;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import models.card.CardType;
import models.comperessedData.CompressedGameMap;
import models.comperessedData.CompressedTroop;
import models.game.map.Position;
import models.gui.CardPane;
import models.gui.DefaultLabel;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;

import static view.BattleView.Constants.NEGATIVE_BUFF_EFFECT;
import static view.BattleView.Constants.POSITIVE_BUFF_EFFECT;

public class MapBox implements PropertyChangeListener {
    private final BattleScene battleScene;
    private final CompressedGameMap gameMap;
    private final Group mapGroup;
    private final Polygon[][] cells = new Polygon[5][9];
    private final double[][] cellsX = new double[5][9];
    private final double[][] cellsY = new double[5][9];
    private final HashMap<CompressedTroop, TroopAnimation> troopAnimationHashMap = new HashMap<>();
    private CompressedTroop selectedTroop = null;
    private ArrayList<CompressedTroop> comboTroops = new ArrayList<>();
    private boolean spellSelected = false;
    private boolean comboSelected = false;
    private CardPane cardPane = null;
    private SelectionType selectionType;
    private DefaultLabel[][] flagLabels = new DefaultLabel[5][9];
    private StackPane[][] flagPanes = new StackPane[5][9];
    private HashMap<String, CardAnimation> collectibleItems = new HashMap<>();
    private Image flag = new Image(new FileInputStream("Client/resources/ui/flag.png"));

    MapBox(BattleScene battleScene, CompressedGameMap gameMap, double x, double y) throws Exception {
        this.battleScene = battleScene;
        this.gameMap = gameMap;
        mapGroup = new Group();
        mapGroup.setLayoutY(y);
        mapGroup.setLayoutX(x);
        makePolygons();
        addFlagLabels();
        addCollectibleItems();
        resetSelection();
        for (CompressedTroop troop : gameMap.getTroops()) {
            updateTroop(null, troop);
        }
        gameMap.addPropertyChangeListener(this);
    }

    private void makePolygons() {
        for (int j = 0; j < 5; j++) {
            double upperWidth = (j * Constants.MAP_DOWNER_WIDTH + (6 - j) * Constants.MAP_UPPER_WIDTH) / 6;
            double downerWidth = ((j + 1) * Constants.MAP_DOWNER_WIDTH + (6 - (j + 1)) * Constants.MAP_UPPER_WIDTH) / 6;
            double upperY = Constants.MAP_HEIGHT * (upperWidth - Constants.MAP_UPPER_WIDTH) /
                    (Constants.MAP_DOWNER_WIDTH - Constants.MAP_UPPER_WIDTH);
            double downerY = Constants.MAP_HEIGHT * (downerWidth - Constants.MAP_UPPER_WIDTH) /
                    (Constants.MAP_DOWNER_WIDTH - Constants.MAP_UPPER_WIDTH);
            for (int i = 0; i < 9; i++) {
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
        for (int j = 0; j < 5; j++) {
            for (int i = 0; i < 9; i++) {
                mapGroup.getChildren().add(new Circle(cellsX[j][i], cellsY[j][i], 2));
            }
        }
    }

    private void addCollectibleItems() {
        for (int j = 0; j < 5; j++) {
            for (int i = 0; i < 9; i++) {
                if (gameMap.getCells()[j][i].getItem() != null) {
                    CardAnimation cardAnimation = new CardAnimation(mapGroup, gameMap.getCells()[j][i].getItem(),
                            cellsY[j][i], cellsX[j][i]);
                    collectibleItems.put(gameMap.getCells()[j][i].getItem().getCardId(), cardAnimation);
                    final int J = j, I = i;
                    cardAnimation.getImageView().setOnMouseClicked(mouseEvent -> clickCell(J, I));
                    cardAnimation.getImageView().setOnMouseExited(mouseEvent -> exitCell(J, I));
                    cardAnimation.getImageView().setOnMouseEntered(mouseEvent -> hoverCell(J, I));
                }
            }
        }
    }

    private void addFlagLabels() {
        for (int j = 0; j < 5; j++) {
            for (int i = 0; i < 9; i++) {
                flagPanes[j][i] = new StackPane();
                flagPanes[j][i].setLayoutX(cellsX[j][i]);
                flagPanes[j][i].setLayoutY(cellsY[j][i]);
                ImageView imageView = new ImageView(flag);
                imageView.setFitHeight(Constants.FLAG_HEIGHT);
                imageView.setFitWidth(Constants.FLAG_WIDTH);
                flagLabels[j][i] = new DefaultLabel(Integer.toString(gameMap.getCell(j, i).getNumberOfFlags()),
                        Constants.FLAG_FONT, Color.BLACK);
                flagPanes[j][i].getChildren().addAll(imageView, flagLabels[j][i]);
                if (gameMap.getCell(j, i).getNumberOfFlags() == 0) {
                    flagPanes[j][i].setVisible(false);
                } else {
                    flagPanes[j][i].setVisible(true);
                }
                final int J = j, I = i;
                flagPanes[J][I].setOnMouseClicked(mouseEvent -> clickCell(J, I));
                flagPanes[J][I].setOnMouseExited(mouseEvent -> exitCell(J, I));
                flagPanes[J][I].setOnMouseEntered(mouseEvent -> hoverCell(J, I));
                mapGroup.getChildren().addAll(flagPanes[j][i]);
            }
        }
    }

    private void updateTroop(CompressedTroop oldTroop, CompressedTroop newTroop) {
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
            animation.moveTo(newTroop.getPosition().getRow(), newTroop.getPosition().getColumn());
        } else {
            try {
                animation = new TroopAnimation(mapGroup, cellsX, cellsY, newTroop.getCard().getSpriteName(),
                        newTroop.getPosition().getRow(), newTroop.getPosition().getColumn(),
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
            Platform.runLater(() -> updateTroop((CompressedTroop) evt.getOldValue(), (CompressedTroop) evt.getNewValue()));
        }
        if (evt.getPropertyName().equals("flag")) {
            Platform.runLater(() -> {
                Position position = (Position) evt.getOldValue();
                Integer newValue = (Integer) evt.getNewValue();
                flagLabels[position.getRow()][position.getColumn()].setText(Integer.toString(newValue));
                if (gameMap.getCell(position.getRow(), position.getColumn()).getNumberOfFlags() == 0) {
                    flagPanes[position.getRow()][position.getColumn()].setVisible(false);
                } else {
                    flagPanes[position.getRow()][position.getColumn()].setVisible(true);
                }
            });
        }
        if (evt.getPropertyName().equals("item")) {
            Platform.runLater(() -> {
                CardAnimation cardAnimation = collectibleItems.get(evt.getOldValue());
                if (cardAnimation != null) {
                    cardAnimation.active();
                    collectibleItems.remove(evt.getOldValue());
                }
            });
        }
        if (evt.getPropertyName().equals("cellEffect")) {
            Platform.runLater(this::updateCellEffects);
        }
    }

    void resetSelection() {
        selectedTroop = null;
        comboTroops.clear();
        spellSelected = false;
        comboSelected = false;
        for (TroopAnimation animation : troopAnimationHashMap.values()) {
            animation.diSelect();
        }
        battleScene.getPlayerBox().refreshComboAndSpell();
        updateMapColors();
    }

    private void exitCell(int j, int i) {
        cells[j][i].setOpacity(Constants.CELLS_DEFAULT_OPACITY);
        if (cardPane != null) {
            mapGroup.getChildren().remove(cardPane);
            cardPane = null;
        }
        CompressedTroop troop = getTroop(j, i);
        if (troop == null)
            return;
        TroopAnimation animation = troopAnimationHashMap.get(troop);
        if (animation == null)
            return;
        if (!(selectedTroop == troop || comboTroops.contains(troop))) {
            animation.diSelect();
        }
    }

    private void hoverCell(int row, int column) {
        CompressedTroop troop = getTroop(row, column);
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
        CompressedTroop currentTroop = getTroop(row, column);
        if (selectionType == SelectionType.INSERTION) {
            if (GameController.getInstance().getAvailableActions().canInsertCard(battleScene.getHandBox().getSelectedCard())) {
                battleScene.getController().insert(battleScene.getHandBox().getSelectedCard(), row, column);
                System.out.println("Insert " + battleScene.getHandBox().getSelectedCard().getCardId());
                battleScene.getHandBox().resetSelection();
                resetSelection();
            }
            return;
        }
        if (selectionType == SelectionType.SELECTION) {
            if (currentTroop != null && currentTroop.getPlayerNumber() == battleScene.getMyPlayerNumber()) {
                selectedTroop = currentTroop;
                updateMapColors();
                SoundEffectPlayer.getInstance().playSound(SoundEffectPlayer.SoundName.select);
                System.out.println("Select " + currentTroop.getCard().getCardId());
            }
            return;
        }
        if (selectedTroop != null && selectedTroop.getPosition().getRow() == row &&
                selectedTroop.getPosition().getColumn() == column) {
            System.out.println("DiSelect");
            SoundEffectPlayer.getInstance().playSound(SoundEffectPlayer.SoundName.select);
            battleScene.getHandBox().resetSelection();
            resetSelection();
            return;
        }
        if (selectionType == SelectionType.SPELL) {
            if (GameController.getInstance().getAvailableActions().canUseSpecialAction(selectedTroop)) {
                battleScene.getController().useSpecialPower(row, column);
                System.out.println(selectedTroop.getCard().getCardId() + " SpecialPower");
                battleScene.getHandBox().resetSelection();
                resetSelection();
            }
            return;
        }
        if (selectionType == SelectionType.COMBO) {
            if (currentTroop != null && currentTroop.getPlayerNumber() == battleScene.getMyPlayerNumber()
                    && currentTroop.getCard().isHasCombo()) {
                if (comboTroops.contains(currentTroop)) {
                    comboTroops.remove(currentTroop);
                    System.out.println("remove " + currentTroop.getCard().getCardId() + " from combos");
                    SoundEffectPlayer.getInstance().playSound(SoundEffectPlayer.SoundName.select);
                } else {
                    comboTroops.add(currentTroop);
                    System.out.println("add " + currentTroop.getCard().getCardId() + " to combos");
                    SoundEffectPlayer.getInstance().playSound(SoundEffectPlayer.SoundName.select);
                }
                updateMapColors();
            } else if (GameController.getInstance().getAvailableActions().canAttack(
                    selectedTroop, row, column)) {
                comboTroops.add(selectedTroop);
                battleScene.getController().comboAttack(comboTroops, currentTroop);
                battleScene.getHandBox().resetSelection();
                resetSelection();
                System.out.println("combo attack");
                SoundEffectPlayer.getInstance().playSound(SoundEffectPlayer.SoundName.attack);
            }
            return;
        }
        if (selectionType == SelectionType.NORMAL) {
            if (GameController.getInstance().getAvailableActions().canAttack(
                    selectedTroop, row, column)) {
                battleScene.getController().attack(selectedTroop, currentTroop);
                System.out.println(selectedTroop + " attacked to " + currentTroop);
                battleScene.getHandBox().resetSelection();
                resetSelection();
                SoundEffectPlayer.getInstance().playSound(SoundEffectPlayer.SoundName.attack);
            } else if (GameController.getInstance().getAvailableActions().canMove(
                    selectedTroop, row, column)) {
                battleScene.getController().move(selectedTroop, row, column);
                System.out.println(selectedTroop.getCard().getCardId() + "moved");
                battleScene.getHandBox().resetSelection();
                resetSelection();
                SoundEffectPlayer.getInstance().playSound(SoundEffectPlayer.SoundName.move);
            }
            return;
        }
    }

    void updateMapColors() {
        updateSelectionType();
        for (int row = 0; row < 5; row++) {
            for (int column = 0; column < 9; column++) {
                if (!battleScene.isMyTurn()) {
                    cells[row][column].setFill(Constants.defaultColor);
                    continue;
                }
                CompressedTroop currentTroop = getTroop(row, column);
                if (selectionType == SelectionType.INSERTION) {
                    if (GameController.getInstance().getAvailableActions().canInsertCard(
                            battleScene.getHandBox().getSelectedCard())) {
                        if (battleScene.getHandBox().getSelectedCard().getType() == CardType.HERO ||
                                battleScene.getHandBox().getSelectedCard().getType() == CardType.MINION) {
                            cells[row][column].setFill(Constants.MOVE_COLOR);
                        } else {
                            cells[row][column].setFill(Constants.SPELL_COLOR);
                        }
                    } else
                        cells[row][column].setFill(Constants.defaultColor);
                    continue;
                }
                if (selectionType == SelectionType.SELECTION) {
                    if (currentTroop != null && currentTroop.getPlayerNumber() == battleScene.getMyPlayerNumber()) {
                        cells[row][column].setFill(Constants.CAN_SELECT_COLOR);
                    } else
                        cells[row][column].setFill(Constants.defaultColor);
                    continue;
                }
                if (selectedTroop != null && selectedTroop.getPosition().getRow() == row &&
                        selectedTroop.getPosition().getColumn() == column) {
                    cells[row][column].setFill(Constants.SELECTED_COLOR);//not important
                    continue;
                }
                if (selectionType == SelectionType.SPELL) {
                    if (GameController.getInstance().getAvailableActions().canUseSpecialAction(selectedTroop)) {
                        cells[row][column].setFill(Constants.SPELL_COLOR);
                    } else
                        cells[row][column].setFill(Constants.defaultColor);
                    continue;
                }
                if (selectionType == SelectionType.COMBO) {
                    if (currentTroop != null && currentTroop.getPlayerNumber() == battleScene.getMyPlayerNumber()
                            && currentTroop.getCard().isHasCombo()) {
                        if (comboTroops.contains(currentTroop))
                            cells[row][column].setFill(Constants.SELECTED_COLOR);
                        else
                            cells[row][column].setFill(Constants.CAN_SELECT_COLOR);
                    } else if (GameController.getInstance().getAvailableActions().canAttack(selectedTroop, row, column))
                        cells[row][column].setFill(Constants.ATTACK_COLOR);
                    else
                        cells[row][column].setFill(Constants.defaultColor);
                    continue;
                }
                if (selectionType == SelectionType.NORMAL) {
                    if (GameController.getInstance().getAvailableActions().canAttack(selectedTroop, row, column))
                        cells[row][column].setFill(Constants.ATTACK_COLOR);
                    else if (GameController.getInstance().getAvailableActions().canMove(selectedTroop, row, column))
                        cells[row][column].setFill(Constants.MOVE_COLOR);
                    else
                        cells[row][column].setFill(Constants.defaultColor);
                    continue;
                }
            }
        }
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
        if (isComboSelected()) {
            selectionType = SelectionType.COMBO;
            return;
        }
        if (isSpellSelected()) {
            selectionType = SelectionType.SPELL;
            return;
        }
        selectionType = SelectionType.NORMAL;
    }

    private CompressedTroop getTroop(int j, int i) {
        for (CompressedTroop troop : troopAnimationHashMap.keySet()) {
            if (troop.getPosition().getRow() == j && troop.getPosition().getColumn() == i)
                return troop;
        }
        return null;
    }

    CompressedTroop getSelectedTroop() {
        return selectedTroop;
    }

    void setSpellSelected() {
        this.spellSelected = true;
    }

    void setComboSelected() {
        this.comboSelected = true;
    }

    boolean isSpellSelected() {
        return spellSelected;
    }

    boolean isComboSelected() {
        return comboSelected;
    }

    CompressedGameMap getGameMap() {
        return gameMap;
    }

    void showAttack(String cardId, String defender) {
        if (cardId == null || defender == null)
            System.out.println("Error0 MapBox");
        CompressedTroop troop = gameMap.getTroop(cardId);
        CompressedTroop defenderTroop = gameMap.getTroop(defender);
        if (troop == null || defenderTroop == null)
            System.out.println("Error1 MapBox");
        else {
            TroopAnimation animation = troopAnimationHashMap.get(troop);
            if (animation == null)
                System.out.println("Error2 MapBox");
            else
                animation.attack(defenderTroop.getPosition().getColumn());
        }
    }

    void showDefend(String defender, String attacker) {
        CompressedTroop troop = gameMap.getTroop(defender);
        CompressedTroop attackerTroop = gameMap.getTroop(attacker);
        if (troop == null || attackerTroop == null)
            System.out.println("Error3 MapBox");
        else {
            TroopAnimation animation = troopAnimationHashMap.get(troop);
            if (animation == null)
                System.out.println("Error4 MapBox");
            else
                animation.hit(attackerTroop.getPosition().getColumn());
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
        INSERTION, SELECTION, COMBO, SPELL, NORMAL
    }
}
