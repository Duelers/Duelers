package models.game.availableActions;


import server.dataCenter.models.card.AttackType;
import models.comperessedData.*;
import server.gameCenter.models.map.Cell;

import java.util.ArrayList;
import java.util.Collections;
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
        calculateMoves(game, ownPlayer);
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


    private void calculateMoves(CompressedGame game, CompressedPlayer ownPlayer) {
        for (CompressedTroop troop : ownPlayer.getTroops()) {
            if (!troop.canMove()) continue;

            Cell currentPosition = troop.getCell();
            ArrayList<Cell> targets = new ArrayList<>();

            int moveSpeed = 2; //Todo make this a troop property with default 2.


            for (int column = currentPosition.getColumn() - 2; column <= currentPosition.getColumn() + 2; column++) {
                int rowDown = currentPosition.getRow() + (2 - Math.abs(column - currentPosition.getColumn()));
                int rowUp = currentPosition.getRow() - (2 - Math.abs(column - currentPosition.getColumn()));

                for (int row = rowUp; row <= rowDown; row++) {
                    if (!CompressedGameMap.isInMap(row, column)) continue;

                    Cell cell = game.getGameMap().getCell(row, column);
                    if (currentPosition.equals(cell)) continue;

                    // Check is an enemy unit is blocking the current path from current position to new position
                    // Note that current implementation only works for movement range of 2.
                    Cell midPoint = new Cell( (cell.getRow() + currentPosition.getRow()) / 2, (cell.getColumn() + currentPosition.getColumn()) / 2 );
                    if (midPoint.getRow() != 0 || midPoint.getColumn() != 0) {
                        if (game.getGameMap().getTroop(midPoint) != null && game.getGameMap().getTroop(midPoint).getPlayerNumber() != ownPlayer.getPlayerNumber()){
                           continue;
                        }
                    }

                    if (game.getGameMap().getTroop(cell) == null) {
                        targets.add(cell);
                    }
                }
            }

            if (targets.size() == 0) continue;

            moves.add(new Move(troop, targets));
        }
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
        if (isTroopProvoked(gameMap, player, troop)) {return false; }
        if (troop.getCard().getDescription().contains("Flying")){ return true; }

        List<Cell> baseMovement = getMovePositions(troop);
        return baseMovement.contains(new Cell(row, column));
    }

    public boolean canAttack(CompressedGameMap gameMap, CompressedPlayer player, CompressedTroop troop, int row, int col) {

        if (troop.getCurrentAp() <= 0){ return false; }

        if (isTroopProvoked(gameMap, player, troop)){
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

    public boolean canDeployMinionOnSquare(CompressedGameMap gameMap, CompressedPlayer player, CompressedCard card, int row, int column){
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
}
