package models.comperessedData;

import models.game.map.Position;

public class CompressedTroop {
    private CompressedCard card;
    private int currentAp;
    private int currentHp;
    private int enemyHitChanges;
    private Position position;
    private boolean canMove;
    private boolean canAttack;
    private boolean isDisarm;
    private boolean noAttackFromWeakerOnes;
    private int numberOfCollectedFlags;
    private int playerNumber;

    //just for testing BattleView


    public CompressedTroop(CompressedTroop troop, int row, int column) {
        this.card = troop.getCard();
        this.currentAp = troop.getCurrentAp();
        this.currentHp = troop.getCurrentHp();
        this.enemyHitChanges = troop.getEnemyHitChanges();
        this.position = new Position(row, column);
        this.canMove = troop.canMove;
        this.canAttack = troop.canAttack;
        this.isDisarm = troop.isDisarm;
        this.noAttackFromWeakerOnes = troop.noAttackFromWeakerOnes;
        this.numberOfCollectedFlags = troop.numberOfCollectedFlags;
        this.playerNumber = troop.playerNumber;
    }

    public CompressedTroop(CompressedCard card, int currentAp, int currentHp, int enemyHitChanges, Position position,
                           boolean canMove, boolean canAttack, boolean isDisarm, boolean noAttackFromWeakerOnes,
                           int numberOfCollectedFlags, int playerNumber) {
        this.card = card;
        this.currentAp = currentAp;
        this.currentHp = currentHp;
        this.enemyHitChanges = enemyHitChanges;
        this.position = position;
        this.canMove = canMove;
        this.canAttack = canAttack;
        this.isDisarm = isDisarm;
        this.noAttackFromWeakerOnes = noAttackFromWeakerOnes;
        this.numberOfCollectedFlags = numberOfCollectedFlags;
        this.playerNumber = playerNumber;
    }

    public CompressedCard getCard() {
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

    public Position getPosition() {
        return position;
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

    public int getNumberOfCollectedFlags() {
        return numberOfCollectedFlags;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }
}
