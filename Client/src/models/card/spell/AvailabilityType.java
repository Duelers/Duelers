package models.card.spell;

public class AvailabilityType {
    private boolean onPut;
    private boolean onAttack;
    private boolean onDeath;
    private boolean continuous;
    private boolean specialPower;
    private boolean onStart;
    private boolean onDefend;

    public AvailabilityType(boolean onPut, boolean onAttack, boolean onDeath, boolean continuous, boolean specialPower, boolean onStart, boolean onDefend) {
        this.onPut = onPut;
        this.onAttack = onAttack;
        this.onDeath = onDeath;
        this.specialPower = specialPower;
        this.onStart = onStart;
        this.continuous = continuous;
        this.onDefend = onDefend;
    }

    public boolean isOnPut() {
        return onPut;
    }

    public boolean isOnAttack() {
        return onAttack;
    }

    public boolean isOnDeath() {
        return onDeath;
    }

    public boolean isContinuous() {
        return continuous;
    }

    public boolean isSpecialPower() {
        return specialPower;
    }

    public boolean isOnDefend() {
        return onDefend;
    }
}