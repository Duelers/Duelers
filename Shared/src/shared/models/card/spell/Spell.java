package shared.models.card.spell;

public class Spell {
    private String spellId;
    private SpellAction action;
    private Target target;
    private AvailabilityType availabilityType;
    private int coolDown;
    private int manaCost;
    private int lastTurnUsed;

    public Spell(String spellId, SpellAction action, Target target, AvailabilityType availabilityType, int coolDown, int manaCost) {
        this.spellId = spellId;
        this.action = action;
        this.target = target;
        this.availabilityType = availabilityType;
        this.coolDown = coolDown;
        this.manaCost = manaCost;
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
        this.manaCost = referenceSpell.manaCost;
        this.lastTurnUsed = referenceSpell.lastTurnUsed;
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

    public int getManaCost() {
        return this.manaCost;
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