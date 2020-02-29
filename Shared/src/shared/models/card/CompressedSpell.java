package shared.models.card;

import shared.models.card.spell.AvailabilityType;
import shared.models.card.spell.Target;


public class CompressedSpell {
    private String spellId;
    private Target target;
    private AvailabilityType availabilityType;
    private int coolDown;
    private int manaCost;
    private int lastTurnUsed;

    public CompressedSpell(String spellId, Target target, AvailabilityType availabilityType, int coolDown, int manaCost, int lastTurnUsed) {
        this.spellId = spellId;
        this.target = target;
        this.availabilityType = availabilityType;
        this.coolDown = coolDown;
        this.manaCost = manaCost;
        this.lastTurnUsed = lastTurnUsed;
    }
}
