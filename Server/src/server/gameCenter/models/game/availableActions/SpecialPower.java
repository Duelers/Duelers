package server.gameCenter.models.game.availableActions;


import server.gameCenter.models.game.Troop;

public class SpecialPower {
    private Troop hero;

    SpecialPower(Troop hero) {
        this.hero = hero;
    }

    public Troop getHero() {
        return hero;
    }
}
