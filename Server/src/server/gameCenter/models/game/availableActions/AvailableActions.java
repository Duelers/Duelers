package server.gameCenter.models.game.availableActions;

import server.dataCenter.models.card.AttackType;
import server.dataCenter.models.card.Card;
import server.gameCenter.models.game.Game;
import server.gameCenter.models.game.Player;
import server.gameCenter.models.game.Troop;
import server.gameCenter.models.map.Cell;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AvailableActions {
    private List<Insert> handInserts = new ArrayList<>();
    private List<Attack> attacks = new ArrayList<>();
    private List<Move> moves = new ArrayList<>();

    public void calculateAvailableActions(Game game) {
        calculateAvailableInsets(game);
        calculateAvailableAttacks(game);
        calculateAvailableMoves(game);
    }

    public void calculateAvailableInsets(Game game) {
        Player ownPlayer = game.getCurrentTurnPlayer();
        handInserts.clear();

        for (Card card : ownPlayer.getHand()) {
            if (ownPlayer.getCurrentMP() >= card.getManaCost()) {
                handInserts.add(new Insert(card));
            }
        }
    }

    public void calculateAvailableAttacks(Game game) {
        Player ownPlayer = game.getCurrentTurnPlayer();
        Player otherPlayer = game.getOtherTurnPlayer();
        attacks.clear();
        for (Troop myTroop : ownPlayer.getTroops()) {
            if (!myTroop.canAttack()) continue;

            ArrayList<Troop> targets = new ArrayList<>();
            for (Troop enemyTroop : otherPlayer.getTroops()) {
                if (enemyTroop.canBeAttackedFromWeakerOnes() && myTroop.getCurrentAp() < enemyTroop.getCurrentAp())
                    continue;

                if (checkRangeForAttack(myTroop, enemyTroop)) continue;

                targets.add(enemyTroop);
            }

            if (targets.size() == 0) continue;

            attacks.add(new Attack(myTroop, targets));
        }
    }
    public void calculateAvailableMoves(Game game) {
        Player ownPlayer = game.getCurrentTurnPlayer();
        moves.clear();
        for (Troop troop : ownPlayer.getTroops()) {
            if (!troop.canMove()) continue;

            Cell currentCell = new Cell(troop.getCell().getRow(), troop.getCell().getColumn());
            ArrayList<Cell> targets = new ArrayList<>();

            // Provoke check
            boolean isProvoked = false;
            List<Cell> neighbourCells = currentCell.getNeighbourCells(5, 9); // Todo 5,9 should be constants
            for (Cell nCell : neighbourCells) {
                if (game.getGameMap().getTroop(nCell) != null) {
                    Troop nearbyUnit = game.getGameMap().getTroop(nCell);
                    // is provoked?
                    if (nearbyUnit.getPlayerNumber() != game.getCurrentTurnPlayer().getPlayerNumber() && nearbyUnit.getCard().getDescription().contains("Provoke")) {
                        isProvoked = true;
                        break;
                    }
                }
            }

            if(!isProvoked) {

                for (int column = currentCell.getColumn() - 2; column <= currentCell.getColumn() + 2; column++) {
                    int rowDown = currentCell.getRow() + (2 - Math.abs(column - currentCell.getColumn()));
                    int rowUp = currentCell.getRow() - (2 - Math.abs(column - currentCell.getColumn()));

                    for (int row = rowUp; row <= rowDown; row++) {
                        if (game.getGameMap().isInMap(row, column)) {
                            Cell cell = game.getGameMap().getCell(row, column);
                            if (currentCell.equals(cell)) continue; // skip own square.
                            if (game.getGameMap().getTroop(cell) != null) continue; // An only move to an empty square

                            // Check is an enemy unit is blocking the current path from current position to new position
                            // Note that current implementation only works for movement range of 2.
                            Cell midPoint = new Cell((cell.getRow() + currentCell.getRow()) / 2, (cell.getColumn() + currentCell.getColumn()) / 2);
                            if (midPoint.getRow() != 0 || midPoint.getColumn() != 0) {
                                if (game.getGameMap().getTroop(midPoint) != null && game.getGameMap().getTroop(midPoint).getPlayerNumber() != ownPlayer.getPlayerNumber()) {
                                    continue;
                                }
                            }
                            targets.add(new Cell(cell.getRow(), cell.getColumn()));
                        }
                    }
                }
            }

            if (targets.size() == 0) continue;

            moves.add(new Move(troop, targets));
        }
    }

    private boolean checkRangeForAttack(Troop myTroop, Troop enemyTroop) {
        if (myTroop.getCard().getAttackType() == AttackType.MELEE) {
            return !myTroop.getCell().isNextTo(enemyTroop.getCell());
        } else if (myTroop.getCard().getAttackType() == AttackType.RANGED) {
            return !myTroop.getCell().isNextTo(enemyTroop.getCell()) &&
                    myTroop.getCell().manhattanDistance(enemyTroop.getCell()) <= myTroop.getCard().getRange();
        } else { // HYBRID
            return myTroop.getCell().manhattanDistance(enemyTroop.getCell()) <= myTroop.getCard().getRange();
        }
    }

    public List<Insert> getHandInserts() {
        return Collections.unmodifiableList(handInserts);
    }

    public String printHand(){
        StringBuilder strBuilder = new StringBuilder("| ");

        if (getHandInserts().size() < 1){
            return "| <EMPTY> |";
        }

        getHandInserts().forEach(n -> strBuilder.append(n.getCard().getCardId() + " | "));
        return strBuilder.toString();
    }

    public List<Attack> getAttacks() {
        return Collections.unmodifiableList(attacks);
    }

    public List<Move> getMoves() {
        return Collections.unmodifiableList(moves);
    }
}
