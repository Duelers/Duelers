package server.clientPortal.models.message;


public class CardAnimation {
    private String defender;
    private String attacker;

    // CardAnimation() {}

    CardAnimation(String spellID, String position) {
        this.attacker = spellID;
        this.defender = position;
    }

    public String getDefender() {
        return defender;
    }

    public String getAttacker() {
        return attacker;
    }
}
