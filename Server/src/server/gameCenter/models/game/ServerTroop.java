package server.gameCenter.models.game;

import shared.models.card.Card;
import shared.models.game.Troop;
import shared.models.game.map.Cell;

public class ServerTroop extends Troop {
    public ServerTroop(Card card, int playerNumber) {
        super(card, playerNumber);
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
