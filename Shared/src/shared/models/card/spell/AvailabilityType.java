package shared.models.card.spell;

public class AvailabilityType {
    private boolean onPut;
    private boolean onAttack;
    private boolean onDeath;
    private boolean onDefend;

    public AvailabilityType(AvailabilityType availabilityType) {
        this.onPut = availabilityType.onPut;
        this.onAttack = availabilityType.onAttack;
        this.onDeath = availabilityType.onDeath;
        this.onDefend = availabilityType.onDefend;
    }

    public AvailabilityType(boolean onPut, boolean onAttack, boolean onDeath, boolean continuous) {
        this.onPut = onPut;
        this.onAttack = onAttack;
        this.onDeath = onDeath;
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

    public boolean isOnDefend() {
        return onDefend;
    }
}