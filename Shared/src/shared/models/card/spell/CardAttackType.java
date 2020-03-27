package shared.models.card.spell;

public class CardAttackType {
    private final boolean melee;
    private final boolean ranged;
    private final boolean hybrid;

    public CardAttackType(boolean melee, boolean ranged, boolean hybrid) {
        this.melee = melee;
        this.ranged = ranged;
        this.hybrid = hybrid;
    }

    public CardAttackType(CardAttackType attackType) {
        this.melee = attackType.melee;
        this.ranged = attackType.ranged;
        this.hybrid = attackType.hybrid;
    }

    public boolean isMelee() {
        return melee;
    }

    public boolean isRanged() {
        return ranged;
    }

    public boolean isHybrid() {
        return hybrid;
    }
}