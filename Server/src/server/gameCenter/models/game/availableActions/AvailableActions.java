package server.gameCenter.models.game.availableActions;

import server.dataCenter.models.card.ServerCard;
import server.gameCenter.models.map.GameMap;
import shared.models.card.AttackType;
import server.gameCenter.models.game.Game;
import server.gameCenter.models.game.Player;
import server.gameCenter.models.game.ServerTroop;
import shared.models.game.availableactions.BaseAvailableActions;

import java.util.*;

public class AvailableActions extends BaseAvailableActions<
        Insert, Attack, Move, ServerTroop, GameMap, Game, ServerCard, Player> {

    public AvailableActions() throws NoSuchMethodException {
        super(Move.class.getDeclaredConstructor(ServerTroop.class, ArrayList.class));
    }

    public void calculateAvailableActions(Game game) {
        calculateAvailableInserts(game);
        calculateAvailableAttacks(game);
        calculateMoves(game);
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
            if (myTroop.getCurrentAp() <= 0) {
                continue;
            }

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
}
