package server.gameCenter.models.game.availableActions;

import javafx.util.Pair;
import server.dataCenter.models.card.ServerCard;
import shared.models.card.AttackType;
import server.gameCenter.models.game.Game;
import server.gameCenter.models.game.Player;
import server.gameCenter.models.game.ServerTroop;
import shared.models.game.map.Cell;

import java.util.*;

public class AvailableActions {
    private List<Insert> handInserts = new ArrayList<>();
    private List<Attack> attacks = new ArrayList<>();
    private List<Move> moves = new ArrayList<>();

    public void calculateAvailableActions(Game game) {
        calculateAvailableInserts(game);
        calculateAvailableAttacks(game);
        calculateAvailableMoves(game);
    }

    public void calculateAvailableInserts(Game game) {
        Player ownPlayer = game.getCurrentTurnPlayer();
        handInserts.clear();

        for (ServerCard card : ownPlayer.getHand()) {
            if (ownPlayer.getCurrentMP() >= card.getManaCost()) {
                handInserts.add(new Insert(card));
            }
        }
    }

    public void calculateAvailableAttacks(Game game) {
        Player ownPlayer = game.getCurrentTurnPlayer();
        Player otherPlayer = game.getOtherTurnPlayer();
        attacks.clear();
        for (ServerTroop myTroop : ownPlayer.getTroops()) {
            if (!myTroop.canAttack()) continue;
            if (myTroop.getCurrentAp() <= 0){ continue;}

            ArrayList<ServerTroop> targets = new ArrayList<>();
            for (ServerTroop enemyTroop : otherPlayer.getTroops()) {
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
        for (ServerTroop troop : ownPlayer.getTroops()) {
            ArrayList<Cell> troopMoves = calculateAvailableMovesForTroop(game, troop);

            if (troopMoves.size() > 0) {
                moves.add(new Move(troop, troopMoves));
            }
        }
    }


    private ArrayList<Cell> calculateAvailableMovesForTroop(Game game, ServerTroop troop) {
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
                    ServerTroop troopInSpace = game.getGameMap().getTroop(adjacentCell);

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

    private boolean getIsProvoked(Game game, Cell troopCell) {
        boolean isProvoked = false;
        List<Cell> neighbourCells = game.getGameMap().getNearbyCells(troopCell);
        for (Cell nCell : neighbourCells) {
            if (game.getGameMap().getTroop(nCell) != null) {
                ServerTroop nearbyUnit = game.getGameMap().getTroop(nCell);
                // is provoked?
                if (nearbyUnit.getPlayerNumber() != game.getCurrentTurnPlayer().getPlayerNumber() && nearbyUnit.getCard().getDescription().contains("Provoke")) {
                    isProvoked = true;
                    break;
                }
            }
        }
        return isProvoked;
    }

    private boolean checkRangeForAttack(ServerTroop myTroop, ServerTroop enemyTroop) {
        if (myTroop.getCard().getAttackType().equals(AttackType.MELEE)) {
            return !myTroop.getCell().isNearbyCell(enemyTroop.getCell());
        } else if (myTroop.getCard().getAttackType().equals(AttackType.RANGED)) {
            return !myTroop.getCell().isNearbyCell(enemyTroop.getCell()) &&
                    myTroop.getCell().manhattanDistance(enemyTroop.getCell()) <= myTroop.getCard().getRange();
        } else { // HYBRID
            return myTroop.getCell().manhattanDistance(enemyTroop.getCell()) <= myTroop.getCard().getRange();
        }
    }

    public List<Insert> getHandInserts() {
        return Collections.unmodifiableList(handInserts);
    }

    public String printHand() {
        StringBuilder strBuilder = new StringBuilder("| ");

        if (getHandInserts().size() < 1) {
            return "| <EMPTY> |";
        }

        getHandInserts().forEach(n -> strBuilder.append(n.getCard().getCardId()).append(" | "));
        return strBuilder.toString();
    }

    public List<Attack> getAttacks() {
        return Collections.unmodifiableList(attacks);
    }

    public List<Move> getMoves() {
        return Collections.unmodifiableList(moves);
    }
}
