package models.card;

import models.ICard;
import models.card.spell.Spell;
import models.exceptions.InputException;
import shared.models.card.AttackType;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

public class EditableCard implements ICard {
    private transient PropertyChangeSupport support = new PropertyChangeSupport(this);
    private String name;
    private String description;
    private String cardId;
    private String spriteName;
    private CardType type;
    private ArrayList<Spell> spells = new ArrayList<>();
    private int defaultAp;
    private int defaultHp;
    private int manaCost;
    private int price;
    private AttackType attackType;
    private int range;

    public void addSpell(Spell spell) {
        spells.add(spell);
        support.firePropertyChange("spells", null, spells);
    }

    public void removeSpell(Spell spell) {
        spells.remove(spell);
        support.firePropertyChange("spells", null, spells);
    }

    public void setAttackType(AttackType attackType) {
        AttackType old = this.attackType;
        this.attackType = attackType;
        support.firePropertyChange("attackType", old, attackType);
    }

    public void setRange(int range) {
        this.range = range;
    }

    @Override
    public CardType getType() {
        return type;
    }

    public void setType(CardType type) {
        CardType old = this.type;
        this.type = type;
        support.firePropertyChange("type", old, type);
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        String old = this.name;
        this.name = name;
        this.cardId = name.replaceAll(" ", "");
        support.firePropertyChange("name", old, name);
    }

    @Override
    public String getSpriteName() {
        return spriteName;
    }

    public void setSpriteName(String spriteName) {
        String old = this.spriteName;
        this.spriteName = spriteName;
        support.firePropertyChange("spriteName", old, spriteName);
    }

    @Override
    public int getDefaultAp() {
        return defaultAp;
    }

    public void setDefaultAp(int defaultAp) {
        int old = this.defaultAp;
        this.defaultAp = defaultAp;
        support.firePropertyChange("defaultAp", old, defaultAp);
    }

    @Override
    public int getDefaultHp() {
        return defaultHp;
    }

    public void setDefaultHp(int defaultHp) {
        int old = this.defaultHp;
        this.defaultHp = defaultHp;
        support.firePropertyChange("defaultHp", old, defaultHp);
    }

    @Override
    public int getPrice() {
        return price;
    }

    public void setPrice(String price) {
        int old = this.price;
        this.price = (price.length() == 0 || price.equals("-")) ? 0 : Integer.parseInt(price);
        support.firePropertyChange("price", old, this.price);
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        String old = this.description;
        this.description = description;
        support.firePropertyChange("description", old, description);
    }

    @Override
    public int getManaCost() {
        return manaCost;
    }

    public void setManaCost(int manaCost) {
        int old = this.manaCost;
        this.manaCost = manaCost;
        support.firePropertyChange("manaCost", old, manaCost);
    }

    public void checkValidation() throws InputException {
        if (name == null || name.isEmpty())
            throw new InputException("name is empty");
        if (description == null || description.isEmpty())
            throw new InputException("description is empty");
        if (spriteName == null || spriteName.isEmpty())
            throw new InputException("sprite is empty");
        if ((type == CardType.SPELL) && spells.isEmpty()) {
            throw new InputException("Spell is empty");
        }
    }

    public void addListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    public Card toImmutableCard() {
        return new Card(
                name, cardId, description, spriteName, type,
                spells, defaultAp, defaultHp, manaCost,
                price, attackType, range
        );
    }
}
