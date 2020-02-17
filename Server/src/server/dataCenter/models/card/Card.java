package server.dataCenter.models.card;

import server.clientPortal.models.comperessedData.CompressedCard;
import server.dataCenter.models.card.spell.Spell;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Card {
    private String name;
    private String description;
    private String cardId;
    private String spriteName;
    private CardType type;
    private List<Spell> spells = new ArrayList<>();
    private int defaultAp;
    private int defaultHp;
    private int mannaPoint;
    private int price;
    private AttackType attackType;
    private int range;
    private int remainingNumber = 20;

    public Card(Card referenceCard, String username, int number) {
        this(referenceCard);
        this.cardId = (username + "_" + referenceCard.name + "_" + number).replaceAll(" ", "");
    }

    //dangerous
    public Card(Card referenceCard) {
        this.name = referenceCard.name;
        this.description = referenceCard.description;
        this.cardId = referenceCard.cardId;
        this.spriteName = referenceCard.spriteName;
        this.type = referenceCard.type;
        if (referenceCard.spells != null) {
            for (Spell spell : referenceCard.spells) {
                spells.add(new Spell(spell));
            }
        }
        this.defaultAp = referenceCard.defaultAp;
        this.defaultHp = referenceCard.defaultHp;
        this.mannaPoint = referenceCard.mannaPoint;
        this.price = referenceCard.price;
        this.attackType = referenceCard.attackType;
        this.range = referenceCard.range;
    }

    public Card(String name, String description, CardType cardType, ArrayList<Spell> spells, int defaultAp,
			int defaultHp, int mannaPoint, int price, AttackType attackType, int range) {
		this.name = name;
		this.description = description;
		this.type = cardType;
		this.spells = spells;
		this.defaultAp = defaultAp;
		this.defaultHp = defaultHp;
		this.mannaPoint = mannaPoint;
		this.price = price;
		this.attackType = attackType;
		this.range = range;
	}

	public CompressedCard toCompressedCard() {
        return new CompressedCard(
                name, description, cardId,
                spriteName, type, spells,
                defaultAp, defaultHp, mannaPoint,
                attackType, range, remainingNumber
        );
    }

    @Override
    public boolean equals(Object obj) {
        if (!this.getClass().getName().equals(obj.getClass().getName())) return false;
        Card card = (Card) obj;
        return this.cardId.equalsIgnoreCase(card.cardId);
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public String getCardId() {
        return this.cardId;
    }

    public void setCardId(String cardId) {//TODO:Should be removed!
        this.cardId = cardId;
    }

    public CardType getType() {
        return this.type;
    }

    public List<Spell> getSpells() {
        return Collections.unmodifiableList(spells);
    }

    public int getDefaultAp() {
        return this.defaultAp;
    }

    public int getDefaultHp() {
        return this.defaultHp;
    }

    public int getMannaPoint() {
        return this.mannaPoint;
    }

    public AttackType getAttackType() {
        return this.attackType;
    }

    public int getRange() {
        return this.range;
    }

    public int getPrice() {
        return price;
    }

    public int getRemainingNumber() {
        return remainingNumber;
    }

    public void setRemainingNumber(int number) {
        remainingNumber = number;
    }

    public void increaseRemainingNumber() {
        remainingNumber++;
    }

    public void decreaseRemainingNumber() {
        remainingNumber--;
    }

    public void addSpell(Spell spell) {
        spells.add(spell);
    }
}