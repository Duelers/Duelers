package org.projectcardboard.client.models.game.availableactions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.projectcardboard.client.controller.GameController;
import org.projectcardboard.client.models.game.Game;
import org.projectcardboard.client.models.game.Player;

import org.projectcardboard.client.models.game.map.GameMap;
import shared.models.card.AttackType;
import shared.models.card.Card;
import shared.models.card.CardType;
import shared.models.game.Troop;
import shared.models.game.availableactions.BaseAvailableActions;
import shared.models.game.map.Cell;

public class AvailableActions
    extends BaseAvailableActions<Insert, Attack, Move, Troop, GameMap, Game, Card, Player> {
  private transient int NumTimesReplacedThisTurn = 0;
  private transient int MaxNumReplacePerTurn = 1;

  public AvailableActions() throws NoSuchMethodException {
    super(Move.class.getDeclaredConstructor(Troop.class, ArrayList.class));
  }


  public void calculate(Game game) {
    clearEverything();
    Player ownPlayer = game.getCurrentTurnPlayer();
    Player otherPlayer = game.getOtherTurnPlayer();

    calculateCardInserts(ownPlayer);
    calculateAttacks(ownPlayer, otherPlayer);
    calculateMoves(game);
  }

  private void calculateCardInserts(Player ownPlayer) {
    for (Card card : ownPlayer.getHand()) {
      handInserts.add(new Insert(card));
    }
  }

  private void calculateAttacks(Player ownPlayer, Player otherPlayer) {
    for (Troop myTroop : ownPlayer.getTroops()) {
      if (!myTroop.canAttack())
        continue;

      ArrayList<Troop> targets = new ArrayList<>();
      for (Troop enemyTroop : otherPlayer.getTroops()) {
        if (enemyTroop.isNoAttackFromWeakerOnes()
            && myTroop.getCurrentAp() < enemyTroop.getCurrentAp())
          continue;

        if (!isTargetInRange(myTroop, enemyTroop))
          continue;

        targets.add(enemyTroop);
      }

      if (targets.size() == 0)
        continue;

      attacks.add(new Attack(myTroop, targets));
    }
  }

  private void clearEverything() {
    handInserts.clear();
    attacks.clear();
    moves.clear();
    setNumTimesReplacedThisTurn(0);
  }

  private boolean isTargetInRange(Troop myTroop, Troop enemyTroop) {
    if (myTroop.getCard().getAttackType().equals(AttackType.MELEE)) {
      return myTroop.getCell().isNearbyCell(enemyTroop.getCell());
    } else if (myTroop.getCard().getAttackType().equals(AttackType.RANGED)) {
      return myTroop.getCell().isNearbyCell(enemyTroop.getCell()) || myTroop.getCell()
          .manhattanDistance(enemyTroop.getCell()) <= myTroop.getCard().getRange();
    } else { // HYBRID
      return myTroop.getCell().manhattanDistance(enemyTroop.getCell()) <= myTroop.getCard()
          .getRange();
    }
  }


  private List<Cell> getMovePositions(Troop troop) {
    for (Move move : moves) {
      if (move.getTroop().equals(troop)) {
        return move.getTargets();
      }
    }
    return Collections.emptyList();
  }

  private List<Cell> getAttackPositions(Troop troop) {
    for (Attack attack : attacks) {
      if (attack.getAttackerTroop().equals(troop)) {
        return attack.getDefenders().stream().map(Troop::getCell).collect(Collectors.toList());
      }
    }
    return Collections.emptyList();
  }

  public boolean canInsertCard(Card card) {
    return handInserts.stream().map(Insert::getCard).collect(Collectors.toList()).contains(card);
  }

  public boolean canMove(GameMap gameMap, Player player, Troop troop, int row, int column) {
    if (!troop.canMove()) {
      return false;
    }

    if (isTroopProvoked(gameMap, player, troop)) {
      return false;
    }
    if (troop.getCard().getDescription().contains("Flying")) {
      return true;
    }

    List<Cell> baseMovement = getMovePositions(troop);
    return baseMovement.contains(new Cell(row, column));
  }

  public boolean canAttack(GameMap gameMap, Player player, Troop troop, int row, int col) {

    if (!troop.canAttack()) {
      return false;
    }

    if (troop.getCurrentAp() <= 0) {
      return false;
    }

    if (isTroopProvoked(gameMap, player, troop)) {
      return getAttackPositions(troop).contains(new Cell(row, col)) && gameMap
          .getTroopAtLocation(new Cell(row, col)).getCard().getDescription().contains("Provoke");
    }
    return getAttackPositions(troop).contains(new Cell(row, col));
  }


  private boolean isTroopProvoked(GameMap gameMap, Player player, Troop troop) {
    Cell currentPosition = troop.getCell();
    ArrayList<Cell> neighbourCells = gameMap.getNearbyCells(currentPosition);

    for (Cell cell : neighbourCells) {
      if (gameMap.getTroopAtLocation(cell) != null) {
        Troop nearbyUnit = gameMap.getTroopAtLocation(cell);
        if (nearbyUnit.getPlayerNumber() != player.getPlayerNumber()
            && nearbyUnit.getCard().getDescription().contains("Provoke")) {
          return true;
        }
      }
    }
    return false;
  }

  public boolean canCastSpellOnSquare(GameMap gameMap, Player player, Card card, int row,
      int column) {
    boolean cardIsSingleTarget = card.isSingleTarget();
    boolean cardTargetsUnits = card.isTargetMinion() || card.isTargetHero();

    if (!cardIsSingleTarget || !cardTargetsUnits) {
      return true;
    }

    Cell cell = new Cell(row, column);
    Troop troop = gameMap.getTroopAtLocation(cell);

    if (troop == null) {
      return false;
    }

    boolean troopIsAlly = troop.getPlayerNumber() == player.getPlayerNumber();
    boolean canCastOnSide =
        (card.isTargetAllyUnit() && troopIsAlly) || (card.isTargetEnemyUnit() && !troopIsAlly);

    CardType cardType = troop.getCard().getType();
    boolean canCastOnCardType = (card.isTargetMinion() && cardType == CardType.MINION)
        || (card.isTargetHero() && cardType == CardType.HERO);

    return canCastOnSide && canCastOnCardType;
  }

  public boolean canDeployMinionOnSquare(GameMap gameMap, Player player, Card card, int row,
      int column) {
    // ToDo this duplicates the logic found in Server's "isLegalCellForMinion" function
    Cell cell = new Cell(row, column);

    if (gameMap.getTroopAtLocation(cell) != null) { // square is occupied
      return false;
    }

    if (card.getDescription().contains("Airdrop")) {
      return true;
    }

    for (Troop troop : player.getTroops()) {
      Cell allyPosition = troop.getCell();

      boolean checkRow = Math.abs(cell.getRow() - allyPosition.getRow()) <= 1;
      boolean checkColumn = Math.abs(cell.getColumn() - allyPosition.getColumn()) <= 1;

      if (checkRow && checkColumn) {
        return true;
      }
    }
    return false;
  }

  public Boolean canReplace(Player player) {
    // Cannot replace on enemy turn.
    if (player.getPlayerNumber() != GameController.getInstance().getCurrentGame()
        .getCurrentTurnPlayer().getPlayerNumber()) {
      return false;
    }
    // ToDo make sure this echoes the logic found in player.java (i.e. should not be able to replace
    // when deck is empty).
    return getNumTimesReplacedThisTurn() < getMaxNumReplacePerTurn();
  }

  public void setNumTimesReplacedThisTurn(int number) {
    this.NumTimesReplacedThisTurn = number;
  }

  public int getNumTimesReplacedThisTurn() {
    return this.NumTimesReplacedThisTurn;
  }

  public void setMaxNumReplacePerTurn(int number) {
    this.MaxNumReplacePerTurn = number;
  }

  public int getMaxNumReplacePerTurn() {
    return this.MaxNumReplacePerTurn;
  }

  public boolean haveSufficientMana(Player player, Card card) {
    if (card.getManaCost() == 0) {
      return true; // even if we have -20 mana, a card of 0 cost should always be playable.
    }
    return player.getCurrentMP() >= card.getManaCost();
  }
}
