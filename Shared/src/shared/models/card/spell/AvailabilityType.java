package shared.models.card.spell;

public class AvailabilityType {
  private final boolean onPut;
  private final boolean onAttack;
  private final boolean onCounterAttack;
  private final boolean onDeath;
  private final boolean onEndTurn;
  private final boolean onSpellCast;
  private boolean onDefend;
  private boolean continuous;


  public AvailabilityType(AvailabilityType availabilityType) {
    this.onPut = availabilityType.onPut;
    this.onAttack = availabilityType.onAttack;
    this.onCounterAttack = availabilityType.onCounterAttack;
    this.onDeath = availabilityType.onDeath;
    this.onDefend = availabilityType.onDefend;
    this.continuous = availabilityType.continuous;
    this.onEndTurn = availabilityType.onEndTurn;
    this.onSpellCast = availabilityType.onSpellCast;
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

  public boolean isOnDefend() {
    return onDefend;
  }

  public boolean isOnEndTurn() {
    return onEndTurn;
  }

  public boolean isOnSpellCast() {
    return onSpellCast;
  }

  public boolean isContinuous() {
    return continuous;
  }
}
