package shared.models.game;

import shared.models.card.Card;
import shared.models.game.map.Cell;


public class Troop {
    protected final Card card;
    protected int currentAp;
    protected int currentHp;
    protected int enemyHitChanges;
    protected Cell cell;
    protected boolean canMove;
    protected boolean canAttack;
    protected boolean isDisarm;
    protected boolean cantGetPoison;
    protected boolean cantGetDisarm;
    protected boolean cantGetStun;
    protected boolean dontGiveBadEffect;
    protected boolean noAttackFromWeakerOnes;
    protected boolean disableHolyBuff;
    private final int playerNumber;

    public Troop(Card card, int playerNumber) {
        this.card = card;
        this.currentAp = card.getDefaultAp();
        this.currentHp = card.getDefaultHp();
        this.playerNumber = playerNumber;
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

    public boolean canMove() {
        return this.canMove;
    }

    public boolean canAttack() {
        return this.canAttack;
    }

    public boolean isDisarm() {
        return this.isDisarm;
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

    public boolean isNoAttackFromWeakerOnes() {
        return noAttackFromWeakerOnes;
    }
}