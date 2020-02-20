package localGameCenter.models.game.availableActions;

import localDataCenter.models.card.AttackType;
import localDataCenter.models.card.Card;
import localGameCenter.models.game.Game;
import localGameCenter.models.game.Player;
import localGameCenter.models.game.Troop;
import localGameCenter.models.map.Cell;
import localGameCenter.models.map.Position;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AvailableActions {
    private List<Insert> handInserts = new ArrayList<>();
    private List<Insert> collectibleInserts = new ArrayList<>();
    private List<Attack> attacks = new ArrayList<>();
    private List<Move> moves = new ArrayList<>();

    public void calculateAvailableActions(Game game) {
        calculateAvailableInsets(game);
        calculateAvailableAttacks(game);
        calculateAvailableMoves(game);
    }

    public void calculateAvailableInsets(Game game) {
        Player ownPlayer = game.getCurrentTurnPlayer();
        collectibleInserts.clear();
        handInserts.clear();

        for (Card card : ownPlayer.getHand()) {
            if (ownPlayer.getCurrentMP() >= card.getMannaPoint()) {
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

            Position currentPosition = new Position(troop.getCell());
            ArrayList<Position> targets = new ArrayList<>();

            for (int column = currentPosition.getColumn() - 2; column <= currentPosition.getColumn() + 2; column++) {
                int rowDown = currentPosition.getRow() + (2 - Math.abs(column - currentPosition.getColumn()));
                int rowUp = currentPosition.getRow() - (2 - Math.abs(column - currentPosition.getColumn()));

                for (int row = rowUp; row <= rowDown; row++) {
                    if (game.getGameMap().isInMap(row, column)) {
                        Cell cell = game.getGameMap().getCell(row, column);
                        if (currentPosition.equals(cell)) continue;

                        if (game.getGameMap().getTroop(cell) == null) {
                            targets.add(new Position(cell));
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

    public List<Insert> getCollectibleInserts() {
        return Collections.unmodifiableList(collectibleInserts);
    }

    public List<Attack> getAttacks() {
        return Collections.unmodifiableList(attacks);
    }

    public List<Move> getMoves() {
        return Collections.unmodifiableList(moves);
    }
}
