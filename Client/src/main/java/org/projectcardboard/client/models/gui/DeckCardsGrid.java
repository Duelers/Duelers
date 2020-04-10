package org.projectcardboard.client.models.gui;

import javafx.scene.input.MouseButton;
import org.projectcardboard.client.controller.CollectionMenuController;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.projectcardboard.client.models.account.Collection;
import shared.models.card.Card;
import shared.models.card.ICard;
import org.projectcardboard.client.models.card.Deck;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.projectcardboard.client.controller.SoundEffectPlayer.SoundName.select;
import static org.projectcardboard.client.models.gui.UIConstants.DEFAULT_SPACING;
import static org.projectcardboard.client.models.gui.UIConstants.SCALE;

public class DeckCardsGrid extends GridPane {
  private static final double BUTTONS_WIDTH = 506 * SCALE;
  private static final int COLUMN_NUMBER = 5;
  private static final double WIDTH = 2350 * SCALE;
  private int currentIndex;
  ArrayList<Card> allCards;

  public DeckCardsGrid(Collection collection, Deck deck) throws FileNotFoundException {
    /*
    setHgap(DEFAULT_SPACING * 5);
    setVgap(DEFAULT_SPACING * 5);
    setMinWidth(WIDTH);
    setMaxWidth(WIDTH);

     */
    setHgap(DEFAULT_SPACING/2);
    setVgap(DEFAULT_SPACING * 1);
    setMinWidth(WIDTH/1.5);

    allCards = new ArrayList<>(collection.getHeroes());
    allCards.addAll(collection.getMinions());
    allCards.addAll(collection.getSpells());

    for (int i = 0; i < 10; i++) {
      final ICard card = allCards.get(i);
      VBox deckCardBox = new VBox(-DEFAULT_SPACING);
      deckCardBox.setAlignment(Pos.CENTER);

      DeckCardPane cardPane = new DeckCardPane(card, deck);
      cardPane.setOnMouseClicked(mouseEvent -> {
        MouseButton button = mouseEvent.getButton();
        if (button.equals(MouseButton.PRIMARY)) {
          CollectionMenuController.getInstance().addCardToDeck(cardPane.getDeck(), card.getName());
        } else if (button.equals(MouseButton.SECONDARY)) {
          CollectionMenuController.getInstance().removeCardFromDeck(cardPane.getDeck(),
              card.getName());
        }
      });

      deckCardBox.getChildren().addAll(cardPane);

      add(deckCardBox, i % COLUMN_NUMBER, i / COLUMN_NUMBER);
    }
    currentIndex = 10;
  }

  public void nextPage(){
    if( currentIndex + 10 > allCards.size()){
      System.out.println("next page will exceed deck size");
      return;
    }
    getChildren().clear();
    for (int i = currentIndex; i < currentIndex + 10; i++) {
      final Card card = allCards.get(i);
      CardPane cardPane = new CardPane(card, false, false, null);
      add(cardPane, i % COLUMN_NUMBER, i / COLUMN_NUMBER);
    }
    this.currentIndex += 10;
  }

  public void prevPage(){
    if( currentIndex - 10 < 0){
      System.out.println("next page will exceed deck size");
      return;
    }
    this.currentIndex -= 10;
    getChildren().clear();

    for (int i = currentIndex; i < currentIndex + 10; i++) {
      final Card card = allCards.get(i);
      CardPane cardPane = new CardPane(card, false, false, null);
      add(cardPane, i % COLUMN_NUMBER, i / COLUMN_NUMBER);
    }
  }
}
