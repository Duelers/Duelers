package server.dataCenter.models.card.spell;

public class AvailabilityType {
    private boolean onPut;
    private boolean onAttack;
    private boolean onDeath;
    private boolean specialPower;
    private boolean onStart;
    private boolean onDefend;

    public AvailabilityType(AvailabilityType availabilityType) {
        this.onPut = availabilityType.onPut;
        this.onAttack = availabilityType.onAttack;
        this.onDeath = availabilityType.onDeath;
        this.specialPower = availabilityType.specialPower;
        this.onStart = availabilityType.onStart;
        this.onDefend = availabilityType.onDefend;
    }

    public AvailabilityType(boolean onPut, boolean onAttack, boolean onDeath, boolean continuous, boolean specialPower, boolean onStart) {
        this.onPut = onPut;
        this.onAttack = onAttack;
        this.onDeath = onDeath;
        this.specialPower = specialPower;
        this.onStart = onStart;
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

    public boolean isSpecialPower() {
        return specialPower;
    }

    public boolean isOnStart() {
        return onStart;
    }

    public boolean isOnDefend() {
        return onDefend;
    }
}