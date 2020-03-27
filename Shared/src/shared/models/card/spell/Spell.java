package shared.models.card.spell;

import shared.models.game.map.Cell;

public class Spell {
    private final String spellId;
    private final String fxName;
    private SpellAction action;
    private Target target;
    private AvailabilityType availabilityType;
    private final int coolDown;
    private final int manaCost;
    private int lastTurnUsed;

    public Spell(String spellId, String fxName, SpellAction action, Target target, AvailabilityType availabilityType, int coolDown, int manaCost) {
        this.spellId = spellId;
        this.fxName = fxName;
        this.action = action;
        this.target = target;
        this.availabilityType = availabilityType;
        this.coolDown = coolDown;
        this.manaCost = manaCost;
    }

    public Spell(Spell referenceSpell) {
        this.spellId = referenceSpell.spellId;
        this.fxName = referenceSpell.fxName;
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

    public String getFxName() {
        return this.fxName;
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

    public boolean isSingleTarget() {
        Cell dimensions = this.target.getDimensions();

        if (dimensions == null) {
            return false;
        }

        boolean isSingleTarget = dimensions.getRow() * dimensions.getColumn() == 1;

        return isSingleTarget;
    }
}