package server.gameCenter.models.game;

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

    int getCurrentHp() {
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

    void setCanAttack(boolean can) {
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

    boolean canGetPoison() {
        return !cantGetPoison;
    }

    boolean canGetDisarm() {
        return !cantGetDisarm;
    }

    boolean canGetStun() {
        return !cantGetStun;
    }

    boolean canGiveBadEffect() {
        return !dontGiveBadEffect;
    }

    public boolean canBeAttackedFromWeakerOnes() {
        return !noAttackFromWeakerOnes;
    }

    boolean isHolyBuffDisabling() {
        return disableHolyBuff;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public int getEnemyHitChanges() {
        return enemyHitChanges;
    }

    void changeCurrentAp(int change) {
        currentAp += change;
        if (currentAp < 0) {
            currentAp = 0;
        }
    }

    void changeCurrentHp(int change) {
        currentHp += change;
    }

    void changeEnemyHit(int change) {
        enemyHitChanges += change;
    }

    void setCantGetPoison(boolean cantGetPoison) {
        this.cantGetPoison = cantGetPoison;
    }

    void setCantGetDisarm(boolean cantGetDisarm) {
        this.cantGetDisarm = cantGetDisarm;
    }

    void setCantGetStun(boolean cantGetStun) {
        this.cantGetStun = cantGetStun;
    }

    void setDontGiveBadEffect(boolean dontGiveBadEffect) {
        this.dontGiveBadEffect = dontGiveBadEffect;
    }

    void setNoAttackFromWeakerOnes(boolean noAttackFromWeakerOnes) {
        this.noAttackFromWeakerOnes = noAttackFromWeakerOnes;
    }

    void setDisableHolyBuff(boolean disableHolyBuff) {
        this.disableHolyBuff = disableHolyBuff;
    }
}