package server.dataCenter.models.card;

import shared.models.card.AttackType;
import shared.models.card.Card;
import shared.models.card.CardType;
import shared.models.card.spell.Owner;
import shared.models.card.spell.Spell;
import shared.models.card.spell.Target;
import shared.models.card.spell.TargetCardType;
import shared.models.game.map.Cell;

import java.util.ArrayList;

public class ServerCard extends Card {
  public ServerCard(String name, String cardId, String faction, String description,
      String spriteName, CardType type, ArrayList<Spell> spells, int defaultAp, int defaultHp,
      int manaCost, int price, AttackType attackType, int range, boolean isCustom) {
    super(name, cardId, faction, description, spriteName, type, spells, defaultAp, defaultHp,
        manaCost, price, attackType, range, isCustom);
  }

  public ServerCard(Card referenceCard, String username, int number) {
    super(referenceCard, username, number);
  }

  public ServerCard(Card referenceCard) {
    super(referenceCard);
    setSingleTargetLogic(referenceCard);
  }

  public void setRemainingNumber(int remainingNumber) {
    int old = this.remainingNumber;
    this.remainingNumber = remainingNumber;
    this.support.firePropertyChange("new_value", old, remainingNumber);
  }

  public void setCardId(String cardId) {// TODO:Should be removed!
    this.cardId = cardId;
  }

  public void addSpell(Spell spell) {
    this.spells.add(spell);
  }

  public void setSingleTargetLogic(Card referenceCard) {
    ArrayList<Spell> spells = referenceCard.getSpells();

    if (spells == null) {
      return;
    }

    for (Spell spell : spells) {
      if (spell.isSingleTarget()) {
        this.singleTarget = true;
      } else {
        continue;
      }

      Target target = spell.getTarget();
      TargetCardType cardType = target.getCardType();
      boolean spellTargetsUnit = cardType.isMinion() || cardType.isHero();

      if (!spellTargetsUnit) {
        continue;
      }

      Owner owner = target.getOwner();

      this.targetAllyUnit = owner.isOwn();
      this.targetEnemyUnit = owner.isEnemy();
      this.targetMinion = cardType.isMinion();
      this.targetHero = cardType.isHero();

      break;
    }
  }
}
