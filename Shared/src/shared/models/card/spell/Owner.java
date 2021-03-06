package shared.models.card.spell;

public class Owner {
  private final boolean own;
  private final boolean enemy;

  public Owner(boolean own, boolean enemy) {
    this.own = own;
    this.enemy = enemy;
  }

  public Owner(Owner referenceOwner) {
    this.own = referenceOwner.own;
    this.enemy = referenceOwner.enemy;
  }

  public boolean isOwn() {
    return own;
  }

  public boolean isEnemy() {
    return enemy;
  }
}
