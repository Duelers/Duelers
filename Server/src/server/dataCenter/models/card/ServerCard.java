package server.dataCenter.models.card;

import shared.models.card.AttackType;
import shared.models.card.Card;
import shared.models.card.CardType;
import shared.models.card.spell.Spell;

import java.util.ArrayList;

public class ServerCard extends Card {
    public ServerCard(String name, String cardId, String description, String spriteName, CardType type, ArrayList<Spell> spells, int defaultAp, int defaultHp, int manaCost, int price, AttackType attackType, int range) {
        super(name, cardId, description, spriteName, type, spells, defaultAp, defaultHp, manaCost, price, attackType, range);
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

    public boolean checkIfSameIDs(Card card){
        /*
            the last two characters at the end of each card ID is to differentiate
            each copy from one another. So that when we do card look ups
            ( e.g. getCard(String cardID) ) we know that we are getting
            the specific card we requested, instead of the first card
            that matched
            So to make sure that the two cards we're comparing are the same, we need
            to remove these numbers from the cardID strings before comparing.
         */
        int thisCardIDLength = this.getCardId().length();
        String thisCardID = this.getCardId().substring(0, thisCardIDLength - 2);

        int cardIDLength = card.getCardId().length();
        String cardID = card.getCardId().substring(0, cardIDLength - 2);

        return cardID.equals(thisCardID);
    }
}
