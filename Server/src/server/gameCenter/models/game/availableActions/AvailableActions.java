package server.gameCenter.models.game.availableActions;

import server.dataCenter.models.card.AttackType;
import server.dataCenter.models.card.Card;
import server.dataCenter.models.card.spell.Spell;
import server.gameCenter.models.game.Game;
import server.gameCenter.models.game.Player;
import server.gameCenter.models.game.Troop;
import server.gameCenter.models.map.Cell;
import server.gameCenter.models.map.Position;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AvailableActions {
    private List<Insert> handInserts = new ArrayList<>();
    private List<Insert> collectibleInserts = new ArrayList<>();
    private List<Attack> attacks = new ArrayList<>();
    private SpecialPower specialPower;
    private List<Move> moves = new ArrayList<>();

    public void calculateAvailableActions(Game game) {
        calculateAvailableInsets(game);
        calculateAvailableAttacks(game);
        calculateAvailableSpecialPower(game);
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

        for (Card item : ownPlayer.getCollectedItems()) {
            collectibleInserts.add(new Insert(item));
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

    private void calculateAvailableCombos(Game game) {
        Player ownPlayer = game.getCurrentTurnPlayer();
        Player otherPlayer = game.getOtherTurnPlayer();
        for (Troop enemyTroop : otherPlayer.getTroops()) {
            ArrayList<Troop> attackers = new ArrayList<>();
            for (Troop myTroop : ownPlayer.getTroops()) {
                if (!myTroop.canAttack()) continue;

                if (enemyTroop.canBeAttackedFromWeakerOnes() && myTroop.getCurrentAp() < enemyTroop.getCurrentAp())
                    continue;

                if (checkRangeForAttack(myTroop, enemyTroop)) continue;

                attackers.add(myTroop);
            }

            if (attackers.size() == 0) continue;
        }
    }

    private void calculateAvailableSpecialPower(Game game) {
        Player ownPlayer = game.getCurrentTurnPlayer();
        Troop hero = ownPlayer.getHero();

        if (hero != null) {
            if (hero.getCard().getSpells().isEmpty()) return;
            Spell spell = hero.getCard().getSpells().get(0);

            if (spell != null && !spell.isCoolDown(game.getTurnNumber()) && spell.getMannaPoint() <= ownPlayer.getCurrentMP()) {
                specialPower = new SpecialPower(hero);
            }
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

    public SpecialPower getSpecialPower() {
        return specialPower;
    }

    public List<Move> getMoves() {
        return Collections.unmodifiableList(moves);
    }
}
