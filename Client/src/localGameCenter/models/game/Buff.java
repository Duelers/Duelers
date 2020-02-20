package localGameCenter.models.game;

import localDataCenter.models.card.spell.SpellAction;

public class Buff {
    private SpellAction action;
    private TargetData target;
    private boolean positive;

    Buff(SpellAction action, TargetData target) {
        this.action = new SpellAction(action);
        this.target = target;
        this.positive = evaluate();
    }

    private boolean evaluate() {
        int weight = action.getApChange() +
                action.getHpChange() -
                action.getEnemyHitChanges() -
                action.getRemoveBuffs();
        if (action.isMakeDisarm()) weight--;
        if (action.isMakeDisarm()) weight--;
        if (action.isNoDisarm()) weight++;
        if (action.isNoPoison()) weight++;
        if (action.isNoStun()) weight++;
        if (action.isNoBadEffect()) weight += 3;
        if (action.isNoAttackFromWeakerOnes()) weight++;
        if (action.isDisableHolyBuff()) weight++;
        if (action.isKillsTarget()) weight -= 3;
        return weight >= 0;
    }

    public SpellAction getAction() {
        return action;
    }

    public TargetData getTarget() {
        return target;
    }

    boolean isPositive() {
        return positive;
    }
}