package test.java;

import org.junit.Assert;
import org.junit.Test;
import server.dataCenter.models.card.Deck;
import server.dataCenter.models.card.ServerCard;
import server.exceptions.ClientException;
import server.exceptions.LogicException;
import shared.models.card.AttackType;
import shared.models.card.CardType;

import java.util.ArrayList;

public class Deck_Test {

  int minimumDeckSize = 5;
  int maximumDeckSize = 40;

  private ServerCard makeMinionCard() {
    return new ServerCard(" ", " ", " ", " ", CardType.MINION, new ArrayList<>(), 1, 1, 1, 1,
        AttackType.MELEE, 1);
  }

  private ServerCard makeGeneralCard() {
    return new ServerCard(" ", " ", " ", " ", CardType.HERO, new ArrayList<>(), 1, 1, 1, 1,
        AttackType.MELEE, 1);
  }

  @Test
  public void hasCard_whenMissingGeneral_NotFindGeneral() {
    Deck sut = new Deck("sut");
    ServerCard general = makeGeneralCard();
    boolean actualHasCard = sut.hasCardOrHeroWithId(general.getCardId());

    Assert.assertFalse(actualHasCard);
  }

  @Test
  public void hasCard_whenMissingCard_NotFindCard() throws LogicException {
    Deck sut = new Deck("sut");
    ServerCard minion = makeMinionCard();
    sut.addCard(minion);

    boolean actualHasCard = sut.hasCardOrHeroWithId(minion.getCardId());

    Assert.assertTrue(actualHasCard);
  }

  @Test
  public void hasCard_whenHasGeneral_FindGeneral() throws LogicException {
    Deck sut = new Deck("sut");
    ServerCard minion = makeGeneralCard();
    sut.addCard(minion);

    boolean actualHasCard = sut.hasCardOrHeroWithId(minion.getCardId());

    Assert.assertTrue(actualHasCard);
  }

  @Test
  public void hasCard_whenHasCard_FindCard() throws LogicException {

    Deck sut = new Deck("sut");
    ServerCard minion = makeMinionCard();
    sut.addCard(minion);

    boolean actualHasCard = sut.hasCardOrHeroWithId(minion.getCardId());

    Assert.assertTrue(actualHasCard);
  }

  @Test(expected = ClientException.class)
  public void addCard_whenAddSecondGeneral_throwsClientException() throws LogicException {
    Deck sut = new Deck("sut");
    ServerCard general0 = makeGeneralCard();
    general0.setCardId("0");
    ServerCard general1 = makeGeneralCard();
    general1.setCardId("1");
    sut.addCard(general0);
    sut.addCard(general1);
  }

  @Test
  public void removeCard_whenHasCard_removesCard() throws LogicException {
    Deck sut = new Deck("sut");
    ServerCard minion = makeMinionCard();
    sut.addCard(minion);
    sut.removeCard(minion);

    boolean actualHasCard = sut.hasCardOrHeroWithId(minion.getCardId());

    Assert.assertFalse(actualHasCard);
  }

  @Test(expected = ClientException.class)
  public void removeCard_whenMissingCard_throwClientError() throws LogicException {
    Deck sut = new Deck("sut");
    ServerCard minion = makeMinionCard();
    sut.removeCard(minion);
  }

  @Test
  public void isValid_fine_isTrue() throws LogicException {
    Deck sut = new Deck("sut");
    ServerCard general = makeGeneralCard();
    sut.addCard(general);
    for (int i = 0; i < maximumDeckSize; i++) {
      ServerCard minion = makeMinionCard();
      sut.addCard(minion);
    }

    boolean actualValid = sut.isValid();

    Assert.assertTrue(actualValid);
  }


  @Test
  public void isValid_noGeneral_isFalse() throws LogicException {
    Deck sut = new Deck("sut");
    for (int i = 0; i < maximumDeckSize; i++) {
      ServerCard minion = makeMinionCard();
      sut.addCard(minion);
    }

    boolean actualValid = sut.isValid();

    Assert.assertFalse(actualValid);
  }

  @Test
  public void isValid_notEnoughCards_isFalse() throws LogicException {
    Deck sut = new Deck("sut");
    ServerCard general = makeGeneralCard();
    sut.addCard(general);
    for (int i = 0; i < minimumDeckSize - 1; i++) {
      ServerCard minion = makeMinionCard();
      sut.addCard(minion);
    }

    boolean actualValid = sut.isValid();

    Assert.assertFalse(actualValid);
  }

}
