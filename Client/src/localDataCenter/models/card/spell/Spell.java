package localDataCenter.models.card.spell;

import localGameCenter.models.compressedData.CompressedSpell;

public class Spell {
    private String spellId;
    private SpellAction action;
    private Target target;
    private AvailabilityType availabilityType;
    private int coolDown;
    private int mannaPoint;
    private int lastTurnUsed;

    public Spell(String spellId, SpellAction action, Target target, AvailabilityType availabilityType, int coolDown, int mannaPoint) {
        this.spellId = spellId;
        this.action = action;
        this.target = target;
        this.availabilityType = availabilityType;
        this.coolDown = coolDown;
        this.mannaPoint = mannaPoint;
    }

    public Spell(Spell referenceSpell) {
        this.spellId = referenceSpell.spellId;
        if (referenceSpell.action != null)
            this.action = new SpellAction(referenceSpell.action);
        if (referenceSpell.target != null)
            this.target = new Target(referenceSpell.target);
        if (referenceSpell.availabilityType != null)
            this.availabilityType = new AvailabilityType(referenceSpell.availabilityType);
        this.coolDown = referenceSpell.coolDown;
        this.mannaPoint = referenceSpell.mannaPoint;
        this.lastTurnUsed = referenceSpell.lastTurnUsed;
    }

    public CompressedSpell toCompressedSpell() {
        return new CompressedSpell(spellId, target, availabilityType, coolDown, mannaPoint, lastTurnUsed);
    }

    public String getSpellId() {
        return this.spellId;
    }

    public Target getTarget() {
        return target;
    }

    public AvailabilityType getAvailabilityType() {
        return availabilityType;
    }

    public int getCoolDown() {
        return this.coolDown;
    }

    public int getMannaPoint() {
        return this.mannaPoint;
    }

    public int getLastTurnUsed() {
        return this.lastTurnUsed;
    }

    public void setLastTurnUsed(int turn) {
        this.lastTurnUsed = turn;
    }

    public SpellAction getAction() {
        return action;
    }

    public boolean isCoolDown(int turnNumber) {
        if (lastTurnUsed == 0) {
            return false;
        }
        return lastTurnUsed + coolDown * 2 >= turnNumber;
    }
}