package shared.models.card;

import shared.models.card.spell.Spell;

import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

public class Card implements ICard {
  protected final transient PropertyChangeSupport support = new PropertyChangeSupport(this);
  private final String name;
  private final String description;
  protected String cardId;
  private final String faction;
  private final String spriteName;
  private final CardType type;
  protected final ArrayList<Spell> spells;
  private final int defaultAp;
  private final int defaultHp;
  private final int manaCost;
  private final int price;
  private final AttackType attackType;
  private boolean hasBackstab;
  private int backstab;
  private boolean hasValor;
  private int valor;
  private final int range;
  protected int remainingNumber = 20;
  protected boolean singleTarget;
  protected boolean targetAllyUnit;
  protected boolean targetEnemyUnit;
  protected boolean targetMinion;
  protected boolean targetHero;
  protected boolean isCustom;

  // This is only used in tests right now. All other cards are loaded from json with gson.
  public Card(String name, // TODO refactor other constructors to use this one.
      String cardId, String faction, String description, String spriteName, CardType type,
      ArrayList<Spell> spells, int defaultAp, int defaultHp, int manaCost, int price,
      AttackType attackType, int range, boolean isCustom) {
    this.name = name;
    this.cardId = cardId;
    this.faction = faction;
    this.description = description;
    this.spriteName = spriteName;
    this.type = type;
    this.isCustom = isCustom;
    this.spells = spells;
    this.defaultAp = defaultAp;
    this.defaultHp = defaultHp;
    this.manaCost = manaCost;
    this.price = price;
    this.attackType = attackType;
    this.range = range;
  }

  public Card(Card referenceCard, String username, int number) {
    this(referenceCard);
    this.cardId = (username + "_" + referenceCard.name + "_" + number).replaceAll(" ", "");
  }

  // dangerous
  public Card(Card referenceCard) {
    this.name = referenceCard.name;
    this.description = referenceCard.description;
    this.cardId = referenceCard.cardId;
    this.faction = referenceCard.faction;
    this.spriteName = referenceCard.spriteName;
    this.type = referenceCard.type;
    this.isCustom = referenceCard.isCustom;
    this.spells = new ArrayList<>();
    if (referenceCard.spells != null) {
      for (Spell spell : referenceCard.spells) {
        this.spells.add(new Spell(spell));
      }
    }
    this.defaultAp = referenceCard.defaultAp;
    this.defaultHp = referenceCard.defaultHp;
    this.manaCost = referenceCard.manaCost;
    this.price = referenceCard.price;
    this.attackType = referenceCard.attackType;
    this.hasBackstab = referenceCard.hasBackstab;
    this.backstab = referenceCard.backstab;
    this.hasValor = referenceCard.hasValor;
    this.valor = referenceCard.valor;
    this.range = referenceCard.range;
    this.remainingNumber = referenceCard.remainingNumber;
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Card)) {
      return false;
    }
    if (!this.getClass().getName().equals(obj.getClass().getName()))
      return false;
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

  public String getFaction() {
    return this.faction;
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

  @Override
  public int getManaCost() {
    return this.manaCost;
  }

  @Override
  public boolean isCustom() {
    return isCustom;
  }

  public AttackType getAttackType() {
    return this.attackType;
  }

  public boolean hasBackstab() {
    return hasBackstab;
  }

  public int getBackstab() {
    return backstab;
  }

  public boolean hasValor() {
    return hasValor;
  }

  public int getValor() {
    return valor;
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

  public boolean checkIfSameID(String cardID) {
    int ignoreLastTwoChars = 2;
    String thisCardID = this.cardId.substring(0, this.cardId.length() - ignoreLastTwoChars);
    String otherCardID = cardID.substring(0, cardID.length() - ignoreLastTwoChars);
    return thisCardID.equalsIgnoreCase(otherCardID);
  }

  public int getRemainingNumber() {
    return remainingNumber;
  }

  public boolean isSingleTarget() {
    return singleTarget;
  }

  public boolean isTargetAllyUnit() {
    return targetAllyUnit;
  }

  public boolean isTargetEnemyUnit() {
    return targetEnemyUnit;
  }

  public boolean isTargetMinion() {
    return targetMinion;
  }

  public boolean isTargetHero() {
    return targetHero;
  }
}
