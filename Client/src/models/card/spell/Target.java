package models.card.spell;

import models.game.map.Cell;

public class Target {
    private boolean isRelatedToCardOwnerPosition;
    private boolean isForAroundOwnHero;
    private boolean isRandom;
    private boolean isForDeckCards;
    private Cell dimensions;
    private Owner owner;
    private TargetCardType cardType;
    private CardAttackType attackType;

    public Target(boolean isRelatedToCardOwnerPosition, boolean isForAroundOwnHero, Cell dimensions, boolean isRandom, Owner owner, TargetCardType cardType, CardAttackType attackType, boolean isForDeckCards) {
        this.isRelatedToCardOwnerPosition = isRelatedToCardOwnerPosition;
        this.isForAroundOwnHero = isForAroundOwnHero;
        this.dimensions = dimensions;
        this.isRandom = isRandom;
        this.owner = owner;
        this.cardType = cardType;
        this.attackType = attackType;
        this.isForDeckCards = isForDeckCards;
    }
}
