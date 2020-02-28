package server.dataCenter.models.card.spell;

import shared.models.game.map.Cell;

public class Target {
    private boolean isRelatedToCardOwnerPosition;
    private boolean isForAroundOwnHero;
    private boolean isRandom;
    private boolean isForDeckCards;
    private Cell dimensions;
    private Owner owner;
    private TargetCardType cardType;
    private CardAttackType attackType;

    public Target(Target referenceTarget) {
        this.isRelatedToCardOwnerPosition = referenceTarget.isRelatedToCardOwnerPosition;
        this.isForAroundOwnHero = referenceTarget.isForAroundOwnHero;
        if (referenceTarget.dimensions != null)
            this.dimensions = new Cell(referenceTarget.dimensions.getRow(), referenceTarget.dimensions.getColumn());

        this.isRandom = referenceTarget.isRandom;
        if (referenceTarget.owner != null)
            this.owner = new Owner(referenceTarget.owner);

        if (referenceTarget.cardType != null)
            this.cardType = new TargetCardType(referenceTarget.cardType);

        if (referenceTarget.attackType != null)
            this.attackType = new CardAttackType(referenceTarget.attackType);

        this.isForDeckCards = referenceTarget.isForDeckCards;
    }

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

    public boolean isRelatedToCardOwnerPosition() {
        return isRelatedToCardOwnerPosition;
    }

    public boolean isForAroundOwnHero() {
        return isForAroundOwnHero;
    }

    public boolean isRandom() {
        return isRandom;
    }

    public boolean isForDeckCards() {
        return isForDeckCards;
    }

    public Cell getDimensions() {
        return dimensions;
    }

    public Owner getOwner() {
        return owner;
    }

    public TargetCardType getCardType() {
        return cardType;
    }

    public CardAttackType getAttackType() {
        return attackType;
    }
}
