package server.clientPortal.models.message;

import shared.models.card.Card;

class CardPositionMessage {
  private final Card card;
  private final CardPosition cardPosition;

  CardPositionMessage(Card card, CardPosition cardPosition) {
    this.card = card;
    this.cardPosition = cardPosition;
  }
}
