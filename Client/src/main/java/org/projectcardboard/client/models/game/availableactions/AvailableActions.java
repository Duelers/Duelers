package org.projectcardboard.client.models.game.availableactions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.projectcardboard.client.controller.GameController;
import org.projectcardboard.client.models.compresseddata.CompressedGame;
import org.projectcardboard.client.models.compresseddata.CompressedGameMap;
import org.projectcardboard.client.models.game.Player;

import javafx.util.Pair;
import shared.models.card.AttackType;
import shared.models.card.Card;
import shared.models.game.Troop;
import shared.models.game.map.Cell;

public class AvailableActions {
    private final List<Insert> handInserts = new ArrayList<>();
    private final List<Attack> attacks = new ArrayList<>();
    private final List<Move> moves = new ArrayList<>();
    private int NumTimesReplacedThisTurn = 0;
    private int MaxNumReplacePerTurn = 1;

    public void calculate(CompressedGame game) {
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
            if (!myTroop.canAttack()) continue;

            ArrayList<Troop> targets = new ArrayList<>();
            for (Troop enemyTroop : otherPlayer.getTroops()) {
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
        Player ownPlayer = game.getCurrentTurnPlayer();
        moves.clear();
        for (Troop troop : ownPlayer.getTroops()) {
            ArrayList<Cell> troopMoves = calculateAvailableMovesForTroop(game, troop);

            if (troopMoves.size() > 0) {
                moves.add(new Move(troop, troopMoves));
            }
        }
    }


    private ArrayList<Cell> calculateAvailableMovesForTroop(CompressedGame game, Troop troop) {
        Cell troopCell = troop.getCell();

        HashSet<Cell> walkableCells = new HashSet<>(); //Cells which the unit can move to.
//        walkableCells.add(troopCell);

        boolean isProvoked = getIsProvoked(game, troopCell);
        if (isProvoked || !troop.canMove()) {
            return new ArrayList<>(walkableCells);
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
                    Troop troopInSpace = game.getGameMap().getTroopAtLocation(adjacentCell);

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

        return new ArrayList<>(walkableCells);
    }

    private boolean getIsProvoked(CompressedGame game, Cell troopCell) {
        boolean isProvoked = false;
        List<Cell> neighbourCells = game.getGameMap().getNearbyCells(troopCell);
        for (Cell nCell : neighbourCells) {
            if (game.getGameMap().getTroopAtLocation(nCell) != null) {
                Troop nearbyUnit = game.getGameMap().getTroopAtLocation(nCell);
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
        setNumTimesReplacedThisTurn(0);
    }

    private boolean isTargetInRange(Troop myTroop, Troop enemyTroop) {
        if (myTroop.getCard().getAttackType().equals(AttackType.MELEE)) {
            return myTroop.getCell().isNearbyCell(enemyTroop.getCell());
        } else if (myTroop.getCard().getAttackType().equals(AttackType.RANGED)) {
            return myTroop.getCell().isNearbyCell(enemyTroop.getCell()) ||
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

    public boolean canMove(CompressedGameMap gameMap, Player player, Troop troop, int row, int column) {
        if (!troop.canMove()){
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

    public boolean canAttack(CompressedGameMap gameMap, Player player, Troop troop, int row, int col) {

        if (!troop.canAttack()) {
            return false;
        }

        if (troop.getCurrentAp() <= 0) {
            return false;
        }

        if (isTroopProvoked(gameMap, player, troop)) {
            return getAttackPositions(troop).contains(new Cell(row, col))
                    && gameMap.getTroopAtLocation(new Cell(row, col)).getCard().getDescription().contains("Provoke");
        }
        return getAttackPositions(troop).contains(new Cell(row, col));
    }


    private boolean isTroopProvoked(CompressedGameMap gameMap, Player player, Troop troop) {
        Cell currentPosition = troop.getCell();
        ArrayList<Cell> neighbourCells = gameMap.getNearbyCells(currentPosition);

        for (Cell cell : neighbourCells) {
            if (gameMap.getTroopAtLocation(cell) != null) {
                Troop nearbyUnit = gameMap.getTroopAtLocation(cell);
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

    public boolean canDeployMinionOnSquare(CompressedGameMap gameMap, Player player, Card card, int row, int column) {
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
        if (player.getPlayerNumber() != GameController.getInstance().getCurrentGame().getCurrentTurnPlayer().getPlayerNumber()) {
            return false;
        }
        // ToDo make sure this echoes the logic found in player.java (i.e. should not be able to replace when deck is empty).
        return getNumTimesReplacedThisTurn() < getMaxNumReplacePerTurn();
    }

    public void setNumTimesReplacedThisTurn(int number){
        this.NumTimesReplacedThisTurn = number;
    }

    public int getNumTimesReplacedThisTurn(){
        return this.NumTimesReplacedThisTurn;
    }

    public void setMaxNumReplacePerTurn(int number){
        this.MaxNumReplacePerTurn = number;
    }

    public int getMaxNumReplacePerTurn(){
        return this.MaxNumReplacePerTurn;
    }

    public boolean haveSufficientMana(Player player, Card card){
        return player.getCurrentMP() >= card.getManaCost();
    }
}
