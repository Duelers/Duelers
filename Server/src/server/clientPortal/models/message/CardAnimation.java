package server.clientPortal.models.message;


public class CardAnimation {
  private final String defender;
  private final String attacker;

  CardAnimation(String spellID, String position) {
    this.attacker = spellID;
    this.defender = position;
  }
}
