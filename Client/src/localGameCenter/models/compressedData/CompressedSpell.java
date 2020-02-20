package localGameCenter.models.compressedData;

import localDataCenter.models.card.spell.AvailabilityType;
import localDataCenter.models.card.spell.Target;

public class CompressedSpell {
    private String spellId;
    private Target target;
    private AvailabilityType availabilityType;
    private int coolDown;
    private int mannaPoint;
    private int lastTurnUsed;

    public CompressedSpell(String spellId, Target target, AvailabilityType availabilityType, int coolDown, int mannaPoint, int lastTurnUsed) {
        this.spellId = spellId;
        this.target = target;
        this.availabilityType = availabilityType;
        this.coolDown = coolDown;
        this.mannaPoint = mannaPoint;
        this.lastTurnUsed = lastTurnUsed;
    }
}
