package shared.models.account;

import shared.models.card.Card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BaseCollection<CardType extends Card> {
  protected final List<CardType> heroes = new ArrayList<>();
  protected final List<CardType> minions = new ArrayList<>();
  protected final List<CardType> spells = new ArrayList<>();

  /**
   * When two cards cost the same mana, sort alphabetically by name. Note that the current
   * implementation only sorts by first character. Thus Az could appear before Ab, but Cz is always
   * before Da.
   */
  protected transient final Comparator<CardType> compareCostThenName =
      Comparator.comparingInt(CardType::getManaCost).thenComparingInt(c -> c.getCardId().charAt(0));

  public void sort() {
    heroes.sort(compareCostThenName);
    minions.sort(compareCostThenName);
    spells.sort(compareCostThenName);
  }

  public List<CardType> getHeroes() {
    return Collections.unmodifiableList(heroes);
  }

  public List<CardType> getMinions() {
    return Collections.unmodifiableList(minions);
  }

  public List<CardType> getSpells() {
    return Collections.unmodifiableList(spells);
  }

}
