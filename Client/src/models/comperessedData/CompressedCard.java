package models.comperessedData;

import models.ICard;
import shared.models.card.AttackType;
import shared.models.card.CardType;

import java.util.Objects;

public class CompressedCard implements ICard {
    private String name;
    private String description;
    private String cardId;
    private String spriteName;
    private CardType type;
    private CompressedSpell spell;//just for hero
    private int defaultAp;
    private int defaultHp;
    private int manaCost;
    private AttackType attackType;
    private int range;

    //just for testing BattleView
    public CompressedCard(String spriteName, String description, String cardId, CardType type, CompressedSpell spell,
                          int defaultAp, int defaultHp, int manaCost, AttackType attackType, int range) {
        name = "...";
        this.spriteName = spriteName;
        this.description = description;
        this.cardId = cardId;
        this.type = type;
        this.spell = spell;
        this.defaultAp = defaultAp;
        this.defaultHp = defaultHp;
        this.manaCost = manaCost;
        this.attackType = attackType;
        this.range = range;
    }
  
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CompressedCard)) return false;
        CompressedCard that = (CompressedCard) o;
        return Objects.equals(cardId, that.cardId);
    }

    @Override
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCardId() {
        return cardId;
    }

    public String getSpriteName() {
        return spriteName;
    }

    @Override
    public CardType getType() {
        return type;
    }

    public CompressedSpell getSpell() {
        return spell;
    }

    @Override
    public int getDefaultAp() {
        return defaultAp;
    }

    @Override
    public int getDefaultHp() {
        return defaultHp;
    }

    @Override
    public int getPrice() {
        return 0;
    }

    public int getManaCost() {
        return manaCost;
    }

    public AttackType getAttackType() {
        return attackType;
    }

    public int getRange() {
        return range;
    }

}
