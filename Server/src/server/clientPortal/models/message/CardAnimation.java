package server.clientPortal.models.message;


public class CardAnimation {
    private String defender;
    private String attacker;

    CardAnimation(String spellID, String position) {
        this.attacker = spellID;
        this.defender = position;
    }
}
