package shared.models.card.spell;


public class SpellAction {
    private int enemyHitChanges;
    private int apChange;
    private int hpChange;
    private int mpChange;
    private boolean poison;
    private boolean makeStun;
    private boolean makeDisarm;
    private boolean noDisarm;
    private boolean noPoison;
    private boolean noStun;
    private boolean noBadEffect;
    private boolean noAttackFromWeakerOnes;
    private boolean disableHolyBuff;
    private boolean addSpell;
    private boolean killsTarget;
    private boolean isForGladiator;
    private boolean durable;
    private int removeBuffs; // -1 for negative buffs and +1 for positive ones
    private int duration;
    private int delay;
    private Spell carryingSpell;

    public SpellAction(int enemyHitChanges, int apChange, int hpChange, int mpChange, boolean poison, boolean makeStun, boolean makeDisarm, boolean actionAtTheEndOfTurn, boolean noDisarm, boolean noPoison, boolean noStun, boolean noBadEffect, boolean noAttackFromWeakerOnes, boolean disableHolyBuff, boolean addSpell, boolean killsTarget, boolean isForGladiator, boolean durable, int removeBuffs, int duration, int delay, Spell carryingSpell) {
        this.enemyHitChanges = enemyHitChanges;
        this.apChange = apChange;
        this.hpChange = hpChange;
        this.mpChange = mpChange;
        this.poison = poison;
        this.makeStun = makeStun;
        this.makeDisarm = makeDisarm;
        this.noDisarm = noDisarm;
        this.noPoison = noPoison;
        this.noStun = noStun;
        this.noBadEffect = noBadEffect;
        this.noAttackFromWeakerOnes = noAttackFromWeakerOnes;
        this.disableHolyBuff = disableHolyBuff;
        this.addSpell = addSpell;
        this.killsTarget = killsTarget;
        this.isForGladiator = isForGladiator;
        this.durable = durable;
        this.removeBuffs = removeBuffs;
        this.delay = delay;
        this.carryingSpell = carryingSpell;
        if (duration == -1) {
            this.duration = Integer.MAX_VALUE;
            return;
        }
        this.duration = duration;
    }

    public SpellAction(int enemyHitChanges, int apChange, int hpChange, int mpChange, boolean poison, boolean makeStun, boolean makeDisarm, boolean noDisarm, boolean noPoison, boolean noStun, boolean noBadEffect, boolean noAttackFromWeakerOnes, boolean killsTarget, boolean durable, int duration, int delay) {
        this.enemyHitChanges = enemyHitChanges;
        this.apChange = apChange;
        this.hpChange = hpChange;
        this.mpChange = mpChange;
        this.poison = poison;
        this.makeStun = makeStun;
        this.makeDisarm = makeDisarm;
        this.noDisarm = noDisarm;
        this.noPoison = noPoison;
        this.noStun = noStun;
        this.noBadEffect = noBadEffect;
        this.noAttackFromWeakerOnes = noAttackFromWeakerOnes;
        this.killsTarget = killsTarget;
        this.durable = durable;
        this.duration = duration;
        this.delay = delay;
    }


    public SpellAction(SpellAction referenceAction) { // copy constructor
        this.enemyHitChanges = referenceAction.enemyHitChanges;
        this.apChange = referenceAction.apChange;
        this.hpChange = referenceAction.hpChange;
        this.mpChange = referenceAction.mpChange;
        this.poison = referenceAction.poison;
        this.makeStun = referenceAction.makeStun;
        this.makeDisarm = referenceAction.makeDisarm;
        this.noDisarm = referenceAction.noDisarm;
        this.noPoison = referenceAction.noPoison;
        this.noStun = referenceAction.noStun;
        this.noBadEffect = referenceAction.noBadEffect;
        this.noAttackFromWeakerOnes = referenceAction.noAttackFromWeakerOnes;
        this.disableHolyBuff = referenceAction.disableHolyBuff;
        this.addSpell = referenceAction.addSpell;
        this.killsTarget = referenceAction.killsTarget;
        this.isForGladiator = referenceAction.isForGladiator;
        this.durable = referenceAction.durable;
        this.removeBuffs = referenceAction.removeBuffs;
        this.duration = referenceAction.duration;
        this.delay = referenceAction.delay;
        this.carryingSpell = referenceAction.carryingSpell;
    }

    public SpellAction makeCopyAction(int duration, int delay) {
        SpellAction spellAction = new SpellAction(this);
        spellAction.duration = duration;
        spellAction.delay = delay;
        return spellAction;
    }

    public int getEnemyHitChanges() {
        return enemyHitChanges;
    }

    public int getApChange() {
        return apChange;
    }

    public int getHpChange() {
        return hpChange;
    }

    public int getMpChange() {
        return mpChange;
    }

    public boolean isPoison() {
        return poison;
    }

    public boolean isMakeStun() {
        return makeStun;
    }

    public boolean isMakeDisarm() {
        return makeDisarm;
    }

    public boolean isNoDisarm() {
        return noDisarm;
    }

    public boolean isNoPoison() {
        return noPoison;
    }

    public boolean isNoStun() {
        return noStun;
    }

    public boolean isNoBadEffect() {
        return noBadEffect;
    }

    public boolean isNoAttackFromWeakerOnes() {
        return noAttackFromWeakerOnes;
    }

    public boolean isDisableHolyBuff() {
        return disableHolyBuff;
    }

    public boolean isAddSpell() {
        return addSpell;
    }

    public boolean isKillsTarget() {
        return killsTarget;
    }

    public boolean isForGladiator() {
        return isForGladiator;
    }

    public boolean isDurable() {
        return durable;
    }

    public int getRemoveBuffs() {
        return removeBuffs;
    }

    public int getDuration() {
        return duration;
    }

    public int getDelay() {
        return delay;
    }

    public Spell getCarryingSpell() {
        return carryingSpell;
    }

    public void decreaseDuration() {
        duration--;
    }

    public void decreaseDelay() {
        delay--;
    }
}