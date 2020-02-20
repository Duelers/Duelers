package localGameCenter.models.game.availableActions;


import localGameCenter.models.game.Troop;

public class SpecialPower {
    private Troop hero;

    SpecialPower(Troop hero) {
        this.hero = hero;
    }

    public Troop getHero() {
        return hero;
    }
}
