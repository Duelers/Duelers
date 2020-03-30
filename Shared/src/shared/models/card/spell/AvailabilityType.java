package shared.models.card.spell;

public class AvailabilityType {
  private final boolean onPut;
  private final boolean onAttack;
  private final boolean onCounterAttack;
  private final boolean onDeath;
  private final boolean onDeathWatch;
  private final boolean onMove;
  private boolean onDefend;
  private boolean continuous;


  public AvailabilityType(AvailabilityType availabilityType) {
    this.onPut = availabilityType.onPut;
    this.onAttack = availabilityType.onAttack;
    this.onCounterAttack = availabilityType.onCounterAttack;
    this.onDeath = availabilityType.onDeath;
    this.onDefend = availabilityType.onDefend;
    this.onDeathWatch = availabilityType.onDeathWatch;
    this.onMove = availabilityType.onMove;
    this.continuous = availabilityType.continuous;
  }

  public boolean isOnPut() {
    return onPut;
  }

  public boolean isOnAttack() {
    return onAttack;
  }

  public boolean isOnCounterAttack() {
    return onCounterAttack;
  }

  public boolean isOnDeath() {
    return onDeath;
  }

  public boolean isDeathWatch() {
    return onDeathWatch;
  }

  public boolean isOnMove() {
    return onMove;
  }

  public boolean isOnDefend() {
    return onDefend;
  }

  public boolean isContinuous() {
    return continuous;
  }
}
