package models.game.availableActions;

import controller.GameController;
import javafx.util.Pair;
import models.comperessedData.*;
import shared.models.card.AttackType;
import shared.models.card.CompressedCard;
import shared.models.game.map.Cell;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class AvailableActions {
    private List<Insert> handInserts = new ArrayList<>();
    private List<Attack> attacks = new ArrayList<>();
    private List<Move> moves = new ArrayList<>();

    public void calculate(CompressedGame game) {
        clearEverything();
        CompressedPlayer ownPlayer = game.getCurrentTurnPlayer();
        CompressedPlayer otherPlayer = game.getOtherTurnPlayer();

        calculateCardInserts(ownPlayer);
        calculateAttacks(ownPlayer, otherPlayer);
        calculateMoves(game);
    }

    private void calculateCardInserts(CompressedPlayer ownPlayer) {
        for (CompressedCard card : ownPlayer.getHand()) {
            handInserts.add(new Insert(card));
        }
    }

    private void calculateAttacks(CompressedPlayer ownPlayer, CompressedPlayer otherPlayer) {
        for (CompressedTroop myTroop : ownPlayer.getTroops()) {
            if (!myTroop.canAttack()) continue;

            ArrayList<CompressedTroop> targets = new ArrayList<>();
            for (CompressedTroop enemyTroop : otherPlayer.getTroops()) {
                if (enemyTroop.isNoAttackFromWeakerOnes() && myTroop.getCurrentAp() < enemyTroop.getCurrentAp())
                    continue;

                if (!isTargetInRange(myTroop, enemyTroop)) continue;

                targets.add(enemyTroop);
            }

            if (targets.size() == 0) continue;

            attacks.add(new Attack(myTroop, targets));
        }
    }

    public void calculateMoves(CompressedGame game) {
        CompressedPlayer ownPlayer = game.getCurrentTurnPlayer();
        moves.clear();
        for (CompressedTroop troop : ownPlayer.getTroops()) {
            ArrayList<Cell> troopMoves = calculateAvailableMovesForTroop(game, troop);

            if (troopMoves.size() > 0) {
                moves.add(new Move(troop, troopMoves));
            }
        }
    }


    private ArrayList<Cell> calculateAvailableMovesForTroop(CompressedGame game, CompressedTroop troop) {
        Cell troopCell = troop.getCell();

        HashSet<Cell> walkableCells = new HashSet<>(); //Cells which the unit can move to.
//        walkableCells.add(troopCell);

        boolean isProvoked = getIsProvoked(game, troopCell);
        if (isProvoked || !troop.canMove()) {
            ArrayList<Cell> walkableCellsList = new ArrayList<>(walkableCells);
            return walkableCellsList;
        }

        HashSet<Cell> seenCells = new HashSet<>();
        seenCells.add(troopCell);

        int moveSpeed = 2; // Todo make a troop property.

        //Cells which the unit can move through.
        //Pair of <Cell>, <Remaining move spaces>
        ArrayList<Pair<Cell, Integer>> pathableFrontier = new ArrayList<>();
        pathableFrontier.add(new Pair<>(troop.getCell(), moveSpeed));

        while (pathableFrontier.size() > 0) {
            Pair<Cell, Integer> currentCellMove = pathableFrontier.remove(0);
            Cell currentCell = currentCellMove.getKey();
            Integer remainingMovement = currentCellMove.getValue();

            if (remainingMovement > 0) {
                ArrayList<Cell> manhattanAdjacentCells = game.getGameMap().getManhattanAdjacentCells(currentCell);
                for (Cell adjacentCell : manhattanAdjacentCells) {
                    CompressedTroop troopInSpace = game.getGameMap().getTroop(adjacentCell);

                    boolean blockedByAnything = troopInSpace != null;
                    if (!blockedByAnything) {
                        if (!seenCells.contains(adjacentCell)) {
                            walkableCells.add(adjacentCell);
                        }
                    }

                    boolean blockedByEnemy = blockedByAnything
                            && troopInSpace.getPlayerNumber() != game.getCurrentTurnPlayer().getPlayerNumber();
                    if (!blockedByEnemy) {
                        pathableFrontier.add(new Pair<>(adjacentCell, remainingMovement - 1));
                    }
                    seenCells.add(adjacentCell);
                }
            }
        }

        ArrayList<Cell> walkableCellsList = new ArrayList<>(walkableCells);
        return walkableCellsList;
    }

    private boolean getIsProvoked(CompressedGame game, Cell troopCell) {
        boolean isProvoked = false;
        List<Cell> neighbourCells = game.getGameMap().getNearbyCells(troopCell);
        for (Cell nCell : neighbourCells) {
            if (game.getGameMap().getTroop(nCell) != null) {
                CompressedTroop nearbyUnit = game.getGameMap().getTroop(nCell);
                // is provoked?
                if (nearbyUnit.getPlayerNumber() != game.getCurrentTurnPlayer().getPlayerNumber() && nearbyUnit.getCard().getDescription().contains("Provoke")) {
                    isProvoked = true;
                    break;
                }
            }
        }
        return isProvoked;
    }


    private void clearEverything() {
        handInserts.clear();
        attacks.clear();
        moves.clear();
    }

    private boolean isTargetInRange(CompressedTroop myTroop, CompressedTroop enemyTroop) {
        if (myTroop.getCard().getAttackType() == AttackType.MELEE) {
            return myTroop.getCell().isNextTo(enemyTroop.getCell());
        } else if (myTroop.getCard().getAttackType() == AttackType.RANGED) {
            return myTroop.getCell().isNextTo(enemyTroop.getCell()) ||
                    myTroop.getCell().manhattanDistance(enemyTroop.getCell()) <= myTroop.getCard().getRange();
        } else { // HYBRID
            return myTroop.getCell().manhattanDistance(enemyTroop.getCell()) <= myTroop.getCard().getRange();
        }
    }

    public List<Insert> getHandInserts() {
        return Collections.unmodifiableList(handInserts);
    }

    public List<Attack> getAttacks() {
        return Collections.unmodifiableList(attacks);
    }

    public List<Move> getMoves() {
        return Collections.unmodifiableList(moves);
    }

    private List<Cell> getMovePositions(CompressedTroop troop) {
        for (Move move : moves) {
            if (move.getTroop().equals(troop)) {
                return move.getTargets();
            }
        }
        return Collections.emptyList();
    }

    private List<Cell> getAttackPositions(CompressedTroop troop) {
        for (Attack attack : attacks) {
            if (attack.getAttackerTroop().equals(troop)) {
                return attack.getDefenders().stream().map(CompressedTroop::getCell).collect(Collectors.toList());
            }
        }
        return Collections.emptyList();
    }

    public boolean canInsertCard(CompressedCard card) {
        return handInserts.stream().map(Insert::getCard).collect(Collectors.toList()).contains(card);
    }

    public boolean canMove(CompressedGameMap gameMap, CompressedPlayer player, CompressedTroop troop, int row, int column) {
        if (isTroopProvoked(gameMap, player, troop)) {
            return false;
        }
        if (troop.getCard().getDescription().contains("Flying")) {
            return true;
        }

        List<Cell> baseMovement = getMovePositions(troop);
        return baseMovement.contains(new Cell(row, column));
    }

    public boolean canAttack(CompressedGameMap gameMap, CompressedPlayer player, CompressedTroop troop, int row, int col) {

        if (troop.getCurrentAp() <= 0) {
            return false;
        }

        if (isTroopProvoked(gameMap, player, troop)) {
            return getAttackPositions(troop).contains(new Cell(row, col))
                    && gameMap.getTroop(new Cell(row, col)).getCard().getDescription().contains("Provoke");
        }
        return getAttackPositions(troop).contains(new Cell(row, col));
    }


    private boolean isTroopProvoked(CompressedGameMap gameMap, CompressedPlayer player, CompressedTroop troop) {
        Cell currentPosition = troop.getCell();
        ArrayList<Cell> neighbourCells = gameMap.getNearbyCells(currentPosition);

        for (Cell cell : neighbourCells) {
            if (gameMap.getTroop(cell) != null) {
                CompressedTroop nearbyUnit = gameMap.getTroop(cell);
                if (nearbyUnit.getPlayerNumber() != player.getPlayerNumber() && nearbyUnit.getCard().getDescription().contains("Provoke")) {
                    return true;
                }
            }
        }
        return false;
    }


    // ToDo fix UI targeting for Spells
    // public boolean canDeploySpellOnSquare(CompressedGame gameMap, CompressedPlayer player, CompressedCard card, int row, int column){
    //}

    public boolean canDeployMinionOnSquare(CompressedGameMap gameMap, CompressedPlayer player, CompressedCard card, int row, int column) {
        // ToDo this duplicates the logic found in Server's "isLegalCellForMinion" function
        Cell cell = new Cell(row, column);

        if (gameMap.getTroop(cell) != null) { // square is occupied
            return false;
        }

        if (card.getDescription().contains("Airdrop")) {
            return true;
        }

        for (CompressedTroop troop : player.getTroops()) {
            Cell allyPosition = troop.getCell();

            boolean checkRow = Math.abs(cell.getRow() - allyPosition.getRow()) <= 1;
            boolean checkColumn = Math.abs(cell.getColumn() - allyPosition.getColumn()) <= 1;

            if (checkRow && checkColumn) {
                return true;
            }
        }
        return false;
    }

    public Boolean canReplace(CompressedPlayer player) {

        // Cannot replace on enemy turn.
        if (player.getPlayerNumber() != GameController.getInstance().getCurrentGame().getCurrentTurnPlayer().getPlayerNumber()) {
            return false;
        }
        // ToDo Other checks to see if replace is valid (e.g. false if already replaced this turn).
        return true;
    }
}
