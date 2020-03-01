package models.comperessedData;

import shared.models.card.Card;
import shared.models.game.map.Cell;

public class CompressedTroop {
    private Card card;
    private int currentAp;
    private int currentHp;
    private int enemyHitChanges;
    private Cell cell;
    private boolean canMove;
    private boolean canAttack;
    private boolean isDisarm;
    private boolean noAttackFromWeakerOnes;
    private int playerNumber;

    //just for testing BattleView
    public CompressedTroop(CompressedTroop troop, int row, int column) {
        this.card = troop.getCard();
        this.currentAp = troop.getCurrentAp();
        this.currentHp = troop.getCurrentHp();
        this.enemyHitChanges = troop.getEnemyHitChanges();
        this.cell = new Cell(row, column);
        this.canMove = troop.canMove;
        this.canAttack = troop.canAttack;
        this.isDisarm = troop.isDisarm;
        this.noAttackFromWeakerOnes = troop.noAttackFromWeakerOnes;
        this.playerNumber = troop.playerNumber;
    }

    public CompressedTroop(Card card, int currentAp, int currentHp, int enemyHitChanges, Cell cell,
                           boolean canMove, boolean canAttack, boolean isDisarm, boolean noAttackFromWeakerOnes, int playerNumber) {
        this.card = card;
        this.currentAp = currentAp;
        this.currentHp = currentHp;
        this.enemyHitChanges = enemyHitChanges;
        this.cell = cell;
        this.canMove = canMove;
        this.canAttack = canAttack;
        this.isDisarm = isDisarm;
        this.noAttackFromWeakerOnes = noAttackFromWeakerOnes;
        this.playerNumber = playerNumber;
    }

    public Card getCard() {
        return card;
    }

    public int getCurrentAp() {
        return currentAp;
    }

    public int getCurrentHp() {
        return currentHp;
    }

    public int getEnemyHitChanges() {
        return enemyHitChanges;
    }

    public Cell getCell() {
        return cell;
    }

    public boolean canMove() {
        return canMove;
    }

    public boolean canAttack() {
        return canAttack;
    }

    public boolean isDisarm() {
        return isDisarm;
    }

    public boolean isNoAttackFromWeakerOnes() {
        return noAttackFromWeakerOnes;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }
}
