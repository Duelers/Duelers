package org.projectcardboard.client.models.message;

public class ChangeCardNumber {
  private final String cardName;
  private final int number;

  ChangeCardNumber(String cardName, int number) {
    this.cardName = cardName;
    this.number = number;
  }

  public String getCardName() {
    return cardName;
  }

  public int getNumber() {
    return number;
  }
}
