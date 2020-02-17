package models.card;


import models.ICard;
import models.card.spell.Spell;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

public class Card implements ICard {
    private transient PropertyChangeSupport support = new PropertyChangeSupport(this);
    private String name;
    private String description;
    private String cardId;
    private String spriteName;
    private CardType type;
    private ArrayList<Spell> spells;
    private int defaultAp;
    private int defaultHp;
    private int mannaPoint;
    private int price;
    private AttackType attackType;
    private int range;
    private int remainingNumber;

    public Card(String name, String cardId, String description, String spriteName, CardType type, ArrayList<Spell> spells, int defaultAp, int defaultHp, int mannaPoint, int price, AttackType attackType, int range) {
        this.name = name;
        this.cardId = cardId;
        this.description = description;
        this.spriteName = spriteName;
        this.type = type;
        this.spells = spells;
        this.defaultAp = defaultAp;
        this.defaultHp = defaultHp;
        this.mannaPoint = mannaPoint;
        this.price = price;
        this.attackType = attackType;
        this.range = range;
    }

    public Card() {
    }

    @Override
    public boolean equals(Object obj) {
        if (!this.getClass().getName().equals(obj.getClass().getName())) return false;
        Card card = (Card) obj;
        return this.cardId.equalsIgnoreCase(card.cardId);
    }

    @Override
    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public String getCardId() {
        return this.cardId;
    }

    public String getSpriteName() {
        return spriteName;
    }

    @Override
    public CardType getType() {
        return this.type;
    }

    public ArrayList<Spell> getSpells() {
        return this.spells;
    }

    @Override
    public int getDefaultAp() {
        return this.defaultAp;
    }

    @Override
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

    @Override
    public int getPrice() {
        return price;
    }

    public boolean nameContains(String cardName) {
        return name.toLowerCase().contains(cardName.toLowerCase());
    }

    public boolean isSameAs(String cardName) {
        return name.equalsIgnoreCase(cardName);
    }

    public int getRemainingNumber() {
        return remainingNumber;
    }

    public void setRemainingNumber(int remainingNumber) {
        int old = this.remainingNumber;
        this.remainingNumber = remainingNumber;
        support.firePropertyChange("new_value", old, remainingNumber);
    }

    public void addListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }


}