package shared.models.card;

import shared.models.card.spell.Spell;

import java.util.List;
import java.util.Objects;

//public class CompressedCard implements ICard {
//    private String name;
//    private String description;
//    private String cardId;
//    private String spriteName;
//    private CardType type;
//    private Spell spell;//just for hero
//    private int defaultAp;
//    private int defaultHp;
//    private int manaCost;
//    private AttackType attackType;
//    private int range;
//    private int remainingNumber;
//
//    //just for testing BattleView
//    public CompressedCard(String spriteName, String description, String cardId, CardType type, Spell spell,
//                          int defaultAp, int defaultHp, int manaCost, AttackType attackType, int range) {
//        name = "...";
//        this.spriteName = spriteName;
//        this.description = description;
//        this.cardId = cardId;
//        this.type = type;
//        this.spell = spell;
//        this.defaultAp = defaultAp;
//        this.defaultHp = defaultHp;
//        this.manaCost = manaCost;
//        this.attackType = attackType;
//        this.range = range;
//    }
//
//    public CompressedCard(String name, String description, String cardId, String spriteName, CardType type,
//                          List<Spell> spells, int defaultAp, int defaultHp, int manaCost,
//                          AttackType attackType, int range, int remainingNumber) {
//        this.name = name;
//        this.description = description;
//        this.cardId = cardId;
//        this.spriteName = spriteName;
//        this.type = type;
//        this.defaultAp = defaultAp;
//        this.defaultHp = defaultHp;
//        this.manaCost = manaCost;
//        this.attackType = attackType;
//        this.range = range;
//        this.remainingNumber = remainingNumber;
//    }
//
//    @Override
//    public String getName() {
//        return name;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public String getCardId() {
//        return cardId;
//    }
//
//    public String getSpriteName() {
//        return spriteName;
//    }
//
//    @Override
//    public CardType getType() {
//        return type;
//    }
//
//    public Spell getSpell() {
//        return spell;
//    }
//
//    @Override
//    public int getDefaultAp() {
//        return defaultAp;
//    }
//
//    @Override
//    public int getDefaultHp() {
//        return defaultHp;
//    }
//
//    @Override
//    public int getPrice() {
//        return 0;
//    }
//
//    public int getManaCost() {
//        return manaCost;
//    }
//
//    public AttackType getAttackType() {
//        return attackType;
//    }
//
//    public int getRange() {
//        return range;
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof CompressedCard)) return false;
//        CompressedCard that = (CompressedCard) o;
//        return Objects.equals(cardId, that.cardId);
//    }
//
//}
