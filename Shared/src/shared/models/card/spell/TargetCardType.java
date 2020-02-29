package shared.models.card.spell;

public class TargetCardType {
    private boolean cell;
    private boolean hero;
    private boolean minion;
    private boolean player;

    public TargetCardType(TargetCardType targetCardType) {
        this.cell = targetCardType.cell;
        this.hero = targetCardType.hero;
        this.minion = targetCardType.minion;
        this.player = targetCardType.player;
    }

    public TargetCardType(boolean cell, boolean hero, boolean minion, boolean player) {
        this.cell = cell;
        this.hero = hero;
        this.minion = minion;
        this.player = player;
    }

    public boolean isCell() {
        return cell;
    }

    public boolean isHero() {
        return hero;
    }

    public boolean isMinion() {
        return minion;
    }

    public boolean isPlayer() {
        return player;
    }
}
