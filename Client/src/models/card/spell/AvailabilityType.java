package models.card.spell;

public class AvailabilityType {
    private boolean onPut;
    private boolean onAttack;
    private boolean onDeath;
    private boolean continuous;
    private boolean onDefend;

    public AvailabilityType(boolean onPut, boolean onAttack, boolean onDeath, boolean continuous, boolean onDefend) {
        this.onPut = onPut;
        this.onAttack = onAttack;
        this.onDeath = onDeath;
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

    public boolean isOnDefend() {
        return onDefend;
    }
}