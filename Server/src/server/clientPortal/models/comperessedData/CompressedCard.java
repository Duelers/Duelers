package server.clientPortal.models.comperessedData;

import shared.models.card.AttackType;
import shared.models.card.CardType;
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
    private int manaCost;
    private AttackType attackType;
    private int range;
    private int remainingNumber;

    public CompressedCard(String name, String description, String cardId, String spriteName, CardType type,
                          List<Spell> spells, int defaultAp, int defaultHp, int manaCost,
                          AttackType attackType, int range, int remainingNumber) {
        this.name = name;
        this.description = description;
        this.cardId = cardId;
        this.spriteName = spriteName;
        this.type = type;
        this.defaultAp = defaultAp;
        this.defaultHp = defaultHp;
        this.manaCost = manaCost;
        this.attackType = attackType;
        this.range = range;
        this.remainingNumber = remainingNumber;
    }
}
