package models.game.availableActions;

import models.card.AttackType;
import models.comperessedData.*;
import models.game.map.Cell;
import server.gameCenter.models.game.Player;
import server.gameCenter.models.game.Troop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

                if (checkRangeForAttack(myTroop, enemyTroop)) continue;

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

            for (int column = currentPosition.getColumn() - 2; column <= currentPosition.getColumn() + 2; column++) {
                int rowDown = currentPosition.getRow() + (2 - Math.abs(column - currentPosition.getColumn()));
                int rowUp = currentPosition.getRow() - (2 - Math.abs(column - currentPosition.getColumn()));

                for (int row = rowUp; row <= rowDown; row++) {
                    if (!CompressedGameMap.isInMap(row, column)) continue;

                    Cell cell = game.getGameMap().getCell(row, column);
                    if (currentPosition.equals(cell)) continue;

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

    private boolean checkRangeForAttack(CompressedTroop myTroop, CompressedTroop enemyTroop) {
        if (myTroop.getCard().getAttackType() == AttackType.MELEE) {
            return !myTroop.getCell().isNextTo(enemyTroop.getCell());
        } else if (myTroop.getCard().getAttackType() == AttackType.RANGED) {
            return myTroop.getCell().isNextTo(enemyTroop.getCell()) ||
                    myTroop.getCell().manhattanDistance(enemyTroop.getCell()) > myTroop.getCard().getRange();
        } else { // HYBRID
            return myTroop.getCell().manhattanDistance(enemyTroop.getCell()) > myTroop.getCard().getRange();
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

    public boolean canMove(CompressedTroop troop, int row, int column) {
        return getMovePositions(troop).contains(new Cell(row, column));
    }
    
    public boolean canDeployMinionOnSquare(CompressedGameMap gameMap, CompressedPlayer player, CompressedCard card, int row, int column){

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
    public boolean canAttack(CompressedTroop troop, int row, int column) {
        return getAttackPositions(troop).contains(new Cell(row, column));
    }

}
