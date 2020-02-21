package models.comperessedData;

import models.card.spell.AvailabilityType;
import models.card.spell.Target;

public class CompressedSpell {
    private String spellId;
    private Target target;
    private AvailabilityType availabilityType;
    private int coolDown;
    private int mannaPoint;
    private int lastTurnUsed;


    public String getSpellId() {
        return spellId;
    }

    public Target getTarget() {
        return target;
    }

    public AvailabilityType getAvailabilityType() {
        return availabilityType;
    }

    public int getCoolDown() {
        return coolDown;
    }

    public int getMannaPoint() {
        return mannaPoint;
    }

    public int getLastTurnUsed() {
        return lastTurnUsed;
    }

    public boolean isCoolDown(int turnNumber) {
        if (lastTurnUsed == 0) {
            return false;
        }
        return lastTurnUsed + coolDown * 2 >= turnNumber;
    }
}
