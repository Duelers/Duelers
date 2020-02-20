package server.clientPortal.models.comperessedData;

import server.dataCenter.models.card.Card;
import server.gameCenter.models.map.Cell;

public class CompressedTroop {
    private CompressedCard card;
    private int currentAp;
    private int currentHp;
    private int enemyHitChanges;
    private Cell cell;
    private boolean canMove = true;
    private boolean canAttack = true;
    private boolean isDisarm;
    private boolean noAttackFromWeakerOnes;
    private int playerNumber;

    public CompressedTroop(Card card, int currentAp, int currentHp, int enemyHitChanges, Cell cell,
                           boolean canMove, boolean canAttack, boolean isDisarm, boolean noAttackFromWeakerOnes, int playerNumber) {
        this.card = card.toCompressedCard();
        this.currentAp = currentAp;
        this.currentHp = currentHp;
        this.enemyHitChanges = enemyHitChanges;
        this.cell = new server.gameCenter.models.map.Cell(cell.getRow(), cell.getColumn());
        this.canMove = canMove;
        this.canAttack = canAttack;
        this.isDisarm = isDisarm;
        this.noAttackFromWeakerOnes = noAttackFromWeakerOnes;
        this.playerNumber = playerNumber;
    }
}
