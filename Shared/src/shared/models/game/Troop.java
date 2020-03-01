package shared.models.game;

import shared.models.card.CompressedTroop;
import shared.models.card.Card;
import shared.models.game.map.Cell;


public class Troop {
    private Card card;
    private int currentAp;
    private int currentHp;
    private int enemyHitChanges;
    private Cell cell;
    private boolean canMove;
    private boolean canAttack;
    private boolean isDisarm;
    private boolean cantGetPoison;
    private boolean cantGetDisarm;
    private boolean cantGetStun;
    private boolean dontGiveBadEffect;
    private boolean noAttackFromWeakerOnes;
    private boolean disableHolyBuff;
    private int playerNumber;

    public Troop(Card card, int playerNumber) {
        this.card = card;
        this.currentAp = card.getDefaultAp();
        this.currentHp = card.getDefaultHp();
        this.playerNumber = playerNumber;
    }

    public Troop(Card card, Cell cell, int playerNumber) {
        this(card, playerNumber);
        this.cell = cell;
    }

    public CompressedTroop toCompressedTroop() {
        return new CompressedTroop(
                card, currentAp, currentHp, enemyHitChanges, cell, canMove, canAttack, isDisarm, noAttackFromWeakerOnes, playerNumber);
    }

    public Card getCard() {
        return this.card;
    }

    public int getCurrentAp() {
        return this.currentAp;
    }

    public int getCurrentHp() {
        return this.currentHp;
    }

    public Cell getCell() {
        return this.cell;
    }

    public void setCell(Cell cell) {
        this.cell = cell;
    }

    public boolean canMove() {
        return this.canMove;
    }

    public void setCanMove(boolean can) {
        this.canMove = can;
        //TODO:Send Message
    }

    public boolean canAttack() {
        return this.canAttack;
    }

    public void setCanAttack(boolean can) {
        this.canAttack = can;
        //TODO:Send Message
    }

    public boolean isDisarm() {
        return this.isDisarm;
    }

    public void setDisarm(boolean disarm) {
        this.isDisarm = disarm;
        //TODO:Send Message
    }

    public boolean canGetPoison() {
        return !cantGetPoison;
    }

    public boolean canGetDisarm() {
        return !cantGetDisarm;
    }

    public boolean canGetStun() {
        return !cantGetStun;
    }

    public boolean canGiveBadEffect() {
        return !dontGiveBadEffect;
    }

    public boolean canBeAttackedFromWeakerOnes() {
        return !noAttackFromWeakerOnes;
    }

    public boolean isHolyBuffDisabling() {
        return disableHolyBuff;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public int getEnemyHitChanges() {
        return enemyHitChanges;
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