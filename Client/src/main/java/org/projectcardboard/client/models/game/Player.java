package org.projectcardboard.client.models.game;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import shared.Constants;
import shared.models.card.Card;
import shared.models.card.CardType;
import shared.models.game.BasePlayer;
import shared.models.game.Troop;

public class Player extends BasePlayer<Card, Troop> {
    private PropertyChangeSupport support = new PropertyChangeSupport(this);

    public Player(String userName, int currentMP,
                  List<Card> hand, List<Card> graveyard, Card nextCard,
                  int playerNumber,
                  List<Troop> troops,
                  Troop hero) {
        super(userName, currentMP, hand, graveyard, nextCard, playerNumber, troops, hero);
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        if (support == null) {
            support = new PropertyChangeSupport(this);
        }
        support.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }

    public void addNextCardToHand() {
        hand.add(nextCard);
        if (support == null) {
            support = new PropertyChangeSupport(this);
        }
        support.firePropertyChange("hand", null, null);
        if (hand.size() > Constants.MAXIMUM_CARD_HAND_SIZE)
            System.out.println("Client Game Error! - current card hand exceeds max card hand size size");
    }


    public void addCardToNext(Card card) {
        if (nextCard != null)
            System.out.println("Compressed Player, addCardToNext, card is null");
        else {
            nextCard = card;
            if (support == null) {
                support = new PropertyChangeSupport(this);
            }
            support.firePropertyChange("next", null, null);
        }
    }

    public void addCardsToHand(int deckSize, Card... drawnCards) {
        System.out.println("Current deck size: " + deckSize);
        System.out.println(nextCard == null);
        if (support == null) {
            support = new PropertyChangeSupport(this);
        }

        this.deckSize = deckSize;
        for (Card drawnCard : drawnCards) {
            if (drawnCard != null && this.hand.size() < Constants.MAXIMUM_CARD_HAND_SIZE) {
                this.hand.add(drawnCard);
                support.firePropertyChange("hand", null, null);
            }
        }
    }

    public void replaceSelectedCard(int selectedCardIndex) {
        hand.set(selectedCardIndex, nextCard);
        removeCardFromNext();
        if (support == null) {
            support = new PropertyChangeSupport(this);
        }
        support.firePropertyChange("replace", null, null);
    }

    public void addCardToGraveYard(Card card) {
        graveyard.add(card);
    }

    public void troopUpdate(Troop troop) {
        if (troops == null)
            troops = new ArrayList<>();
        removeTroop(troop.getCard().getCardId());
        if (troop.getCurrentHp() > 0) {
            troops.add(troop);
            if (troop.getCard().getType().equals(CardType.HERO))
                hero = troop;
        }
    }

    public void removeCardFromHand(String cardId) {
        hand.removeIf(card -> card.getCardId().equalsIgnoreCase(cardId));
        if (support == null) {
            support = new PropertyChangeSupport(this);
        }
        support.firePropertyChange("hand", null, null);
    }

    public void removeCardFromNext() {
        nextCard = null;
        if (support == null) {
            support = new PropertyChangeSupport(this);
        }
        support.firePropertyChange("next", null, null);
    }

    public void removeTroop(String cardId) {
        if (troops == null)
            troops = new ArrayList<>();
        troops.removeIf(troop -> troop.getCard().getCardId().equalsIgnoreCase(cardId));
        if (hero != null && hero.getCard().getCardId().equalsIgnoreCase(cardId))
            hero = null;
    }

    public List<Troop> getTroops() {
        return Collections.unmodifiableList(troops);
    }

    public void setTroops(List<Troop> troops) {//Todo this should be part of construction, not a method.
        this.troops = troops;

        for (Troop troop : troops) {
            if (troop.getCard().getType().equals(CardType.HERO)) {
                hero = troop;
            }
        }
    }

    public Card searchGraveyard(String cardId) {
        for (Card card : graveyard) {
            if (card.getCardId().equalsIgnoreCase(cardId)) {
                return card;
            }
        }
        return null;
    }

    public void setCurrentMP(int currentMP, int turnNumber) {
        this.currentMP = currentMP;
    }


    public int getPlayerNumber() {
        return playerNumber;
    }

    public int getDeckSize(){
        return this.deckSize;
    }
}