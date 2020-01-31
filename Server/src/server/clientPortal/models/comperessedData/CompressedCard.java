package server.clientPortal.models.comperessedData;

import server.dataCenter.models.card.AttackType;
import server.dataCenter.models.card.CardType;
import server.dataCenter.models.card.spell.Spell;

import java.util.List;

public class CompressedCard {
    private String name;
    private String description;
    private String cardId;
    private String spriteName;
    private CardType type;
    private CompressedSpell spell;//just for hero
    private int defaultAp;
    private int defaultHp;
    private int mannaPoint;
    private AttackType attackType;
    private int range;
    private boolean hasCombo;
    private int remainingNumber;

    public CompressedCard(String name, String description, String cardId, String spriteName, CardType type,
                          List<Spell> spells, int defaultAp, int defaultHp, int mannaPoint,
                          AttackType attackType, int range, boolean hasCombo, int remainingNumber) {
        this.name = name;
        this.description = description;
        this.cardId = cardId;
        this.spriteName = spriteName;
        this.type = type;
        this.defaultAp = defaultAp;
        this.defaultHp = defaultHp;
        this.mannaPoint = mannaPoint;
        this.attackType = attackType;
        this.range = range;
        this.hasCombo = hasCombo;
        if (type == CardType.HERO) {
            for (Spell spell : spells) {
                if (spell.getAvailabilityType().isSpecialPower()) {
                    this.spell = spell.toCompressedSpell();
                    break;
                }
            }
        }
        this.remainingNumber = remainingNumber;
    }
}
