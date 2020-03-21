package server.dataCenter.models.card;

import shared.models.card.AttackType;
import shared.models.card.Card;
import shared.models.card.CardType;
import shared.models.card.spell.Spell;

import java.util.ArrayList;

public class ServerCard extends Card {
    public ServerCard(String name, String cardId, String description, String spriteName, String fxName, CardType type, ArrayList<Spell> spells, int defaultAp, int defaultHp, int manaCost, int price, AttackType attackType, int range) {
        super(name, cardId, description, spriteName, fxName, type, spells, defaultAp, defaultHp, manaCost, price, attackType, range);
    }

    public ServerCard(Card referenceCard, String username, int number) {
        super(referenceCard, username, number);
    }

    public ServerCard(Card referenceCard) {
        super(referenceCard);
    }

    public void setRemainingNumber(int remainingNumber) {
        int old = this.remainingNumber;
        this.remainingNumber = remainingNumber;
        this.support.firePropertyChange("new_value", old, remainingNumber);
    }

    public void setCardId(String cardId) {//TODO:Should be removed!
        this.cardId = cardId;
    }

    public void addSpell(Spell spell) {
        this.spells.add(spell);
    }
}
