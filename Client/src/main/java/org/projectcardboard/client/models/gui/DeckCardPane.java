package org.projectcardboard.client.models.gui;

import org.projectcardboard.client.controller.Client;
import javafx.application.Platform;
import shared.models.card.ICard;
import org.projectcardboard.client.models.card.Deck;

import java.beans.PropertyChangeEvent;

public class DeckCardPane extends CardPane {

  DeckCardPane(ICard card, Deck deck) {
    super(card, false, true, deck);
  }

  @Override
  void count(ICard card) {
    Client.getInstance().getAccount().addPropertyChangeListener(this);
    oldCount = deck.count(card);
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if (evt.getPropertyName().equals("decks")) {
      deck = Client.getInstance().getAccount().getDeck(deck.getName());
      if (deck == null) {
        Client.getInstance().getAccount().removePropertyChangeListener(this);
        return;
      }
      int newCount = deck.count(card);
      if (newCount != oldCount) {
        oldCount = newCount;
        Platform.runLater(() -> countLabel.setText("X " + newCount));
      }
    }
  }

  public Deck getDeck() {
    return deck;
  }
}
