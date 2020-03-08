package server.gameCenter.models.game;

import server.dataCenter.models.card.ServerCard;
import shared.models.card.Card;
import shared.models.game.Troop;
import shared.models.game.map.Cell;

public class ServerTroop extends Troop {
    private int remainingMoves;
    private int remainingAttacks;

    public ServerTroop(Card card, int playerNumber) {
        super(card, playerNumber);
        resetRemainingMovesAndAttacks();
    }

    public ServerCard getCard() {
        ServerCard card = new ServerCard(super.getCard());
        return card;
    }

    public void resetRemainingMovesAndAttacks() {
        String description = getCard().getDescription();
        int value;
        if (description.contains("Celerity")) {
            value = 2;
        } else {
            value = 1;
        }
        remainingMoves = value;
        remainingAttacks = value;
    }

    public int getRemainingMoves() {
        return remainingMoves;
    }

    public int getRemainingAttacks() {
        return remainingAttacks;
    }

    public void reduceRemainingMoves() {
        remainingMoves--;
    }

    public void reduceRemainingAttacks() {
        remainingAttacks--;
    }

    public boolean noMovesRemaining() {
        return remainingMoves <= 0;
    }

    public boolean noAttacksRemaining() {
        return remainingAttacks <= 0;
    }

    public void setCell(Cell cell) {
        this.cell = cell;
    }

    public void setCanMove(boolean can) {
        this.canMove = can;
        //TODO:Send Message
    }

    public void setCanAttack(boolean can) {
        this.canAttack = can;
        //TODO:Send Message
    }

    public void setDisarm(boolean disarm) {
        this.isDisarm = disarm;
        //TODO:Send Message
    }

    public void changeCurrentAp(int change) {
        currentAp += change;
        if (currentAp < 0) {
            currentAp = 0;
        }
    }

    public void changeCurrentHp(int change) {
        currentHp += change;
    }

    public void changeEnemyHit(int change) {
        enemyHitChanges += change;
    }

    public void setCantGetPoison(boolean cantGetPoison) {
        this.cantGetPoison = cantGetPoison;
    }

    public void setCantGetDisarm(boolean cantGetDisarm) {
        this.cantGetDisarm = cantGetDisarm;
    }

    public void setCantGetStun(boolean cantGetStun) {
        this.cantGetStun = cantGetStun;
    }

    public void setDontGiveBadEffect(boolean dontGiveBadEffect) {
        this.dontGiveBadEffect = dontGiveBadEffect;
    }

    public void setNoAttackFromWeakerOnes(boolean noAttackFromWeakerOnes) {
        this.noAttackFromWeakerOnes = noAttackFromWeakerOnes;
    }

    public void setDisableHolyBuff(boolean disableHolyBuff) {
        this.disableHolyBuff = disableHolyBuff;
    }
}
