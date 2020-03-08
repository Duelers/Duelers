package shared.models.card.spell;

public class AvailabilityType {
    private final boolean onPut;
    private final boolean onAttack;
    private final boolean onDeath;
    private boolean onDefend;
    private boolean continuous;


    public AvailabilityType(AvailabilityType availabilityType) {
        this.onPut = availabilityType.onPut;
        this.onAttack = availabilityType.onAttack;
        this.onDeath = availabilityType.onDeath;
        this.onDefend = availabilityType.onDefend;
        this.continuous = availabilityType.continuous;
    }

    public AvailabilityType(boolean onPut, boolean onAttack, boolean onDeath, boolean continuous) {
        this.onPut = onPut;
        this.onAttack = onAttack;
        this.onDeath = onDeath;
        this.continuous = continuous;
    }

    public AvailabilityType(boolean onPut, boolean onAttack, boolean onDeath, boolean continuous, boolean onDefend) {
        this.onPut = onPut;
        this.onAttack = onAttack;
        this.onDeath = onDeath;
        this.continuous = continuous;
        this.onDefend = onDefend; //Todo Kill unneeded constructors.
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

    public boolean isContinuous() {
        return continuous;
    }


}