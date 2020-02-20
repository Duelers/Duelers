package localDataCenter.models.card.spell;

public class Owner {
    private boolean own;
    private boolean enemy;

    public Owner(Owner referenceOwner) {
        this.own = referenceOwner.own;
        this.enemy = referenceOwner.enemy;
    }

    public Owner(boolean own, boolean enemy) {
        this.own = own;
        this.enemy = enemy;
    }

    public boolean isOwn() {
        return own;
    }

    public boolean isEnemy() {
        return enemy;
    }
}
