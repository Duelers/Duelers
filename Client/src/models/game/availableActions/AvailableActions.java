package models.game.availableActions;


import models.card.AttackType;
import models.comperessedData.*;
import models.game.map.Position;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class AvailableActions {
    private List<Insert> handInserts = new ArrayList<>();
    private List<Insert> collectibleInserts = new ArrayList<>();
    private List<Attack> attacks = new ArrayList<>();
    private SpecialPower specialPower;
    private List<Move> moves = new ArrayList<>();

    public void calculate(CompressedGame game) {
        clearEverything();
        CompressedPlayer ownPlayer = game.getCurrentTurnPlayer();
        CompressedPlayer otherPlayer = game.getOtherTurnPlayer();

        calculateCardInserts(ownPlayer);
        calculateCollectibles(ownPlayer);
        calculateAttacks(ownPlayer, otherPlayer);
        calculateSpecialPower(game, ownPlayer);
        calculateMoves(game, ownPlayer);
    }

    private void calculateCardInserts(CompressedPlayer ownPlayer) {
        for (CompressedCard card : ownPlayer.getHand()) {
            if (ownPlayer.getCurrentMP() >= card.getMannaPoint()) {
                handInserts.add(new Insert(card));
            }
        }
    }

    private void calculateCollectibles(CompressedPlayer ownPlayer) {
        for (CompressedCard item : ownPlayer.getCollectedItems()) {
            collectibleInserts.add(new Insert(item));
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

    private void calculateSpecialPower(CompressedGame game, CompressedPlayer ownPlayer) {
        CompressedTroop hero = ownPlayer.getHero();

        if (hero != null) {
            CompressedSpell spell = hero.getCard().getSpell();

            if (spell != null && !spell.isCoolDown(game.getTurnNumber()) && spell.getMannaPoint() <= ownPlayer.getCurrentMP()) {
                specialPower = new SpecialPower(hero);
            }
        }
    }

    private void calculateMoves(CompressedGame game, CompressedPlayer ownPlayer) {
        for (CompressedTroop troop : ownPlayer.getTroops()) {
            if (!troop.canMove()) continue;

            Position currentPosition = troop.getPosition();
            ArrayList<Position> targets = new ArrayList<>();

            for (int column = currentPosition.getColumn() - 2; column <= currentPosition.getColumn() + 2; column++) {
                int rowDown = currentPosition.getRow() + (2 - Math.abs(column - currentPosition.getColumn()));
                int rowUp = currentPosition.getRow() - (2 - Math.abs(column - currentPosition.getColumn()));

                for (int row = rowUp; row <= rowDown; row++) {
                    if (!CompressedGameMap.isInMap(row, column)) continue;

                    Position cell = game.getGameMap().getCell(row, column).toPosition();
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
        collectibleInserts.clear();
        attacks.clear();
        moves.clear();
        specialPower = null;
    }

    private boolean checkRangeForAttack(CompressedTroop myTroop, CompressedTroop enemyTroop) {
        if (myTroop.getCard().getAttackType() == AttackType.MELEE) {
            return !myTroop.getPosition().isNextTo(enemyTroop.getPosition());
        } else if (myTroop.getCard().getAttackType() == AttackType.RANGED) {
            return myTroop.getPosition().isNextTo(enemyTroop.getPosition()) ||
                    myTroop.getPosition().manhattanDistance(enemyTroop.getPosition()) > myTroop.getCard().getRange();
        } else { // HYBRID
            return myTroop.getPosition().manhattanDistance(enemyTroop.getPosition()) > myTroop.getCard().getRange();
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

    public SpecialPower getSpecialPower() {
        return specialPower;
    }

    public List<Move> getMoves() {
        return Collections.unmodifiableList(moves);
    }

    private List<Position> getMovePositions(CompressedTroop troop) {
        for (Move move : moves) {
            if (move.getTroop().equals(troop)) {
                return move.getTargets();
            }
        }
        return Collections.emptyList();
    }

    private List<Position> getAttackPositions(CompressedTroop troop) {
        for (Attack attack : attacks) {
            if (attack.getAttackerTroop().equals(troop)) {
                return attack.getDefenders().stream().map(CompressedTroop::getPosition).collect(Collectors.toList());
            }
        }
        return Collections.emptyList();
    }

    public boolean canInsertCard(CompressedCard card) {
        if (handInserts.stream().map(Insert::getCard).collect(Collectors.toList()).contains(card)) return true;
        return collectibleInserts.stream().map(Insert::getCard).collect(Collectors.toList()).contains(card);
    }

    public boolean canMove(CompressedTroop troop, int row, int column) {
        return getMovePositions(troop).contains(new Position(row, column));
    }

    public boolean canAttack(CompressedTroop troop, int row, int column) {
        return getAttackPositions(troop).contains(new Position(row, column));
    }

    public boolean canUseSpecialAction(CompressedTroop troop) {
        return specialPower != null && specialPower.getHero().equals(troop);
    }
}
