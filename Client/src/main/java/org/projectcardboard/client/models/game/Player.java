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
        super(userName, currentMP, hand, graveyard, playerNumber, troops, hero);
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

    public void addCardsToHand(int deckSize, Card... drawnCards) {
        if (support == null) {
            support = new PropertyChangeSupport(this);
        }

        this.deckSize = deckSize;
        System.out.println("Current deck size: " + deckSize);
        for (Card drawnCard : drawnCards) {
            if (drawnCard != null && this.hand.size() < Constants.MAXIMUM_CARD_HAND_SIZE) {
                this.hand.add(drawnCard);
                support.firePropertyChange("hand", null, null);
            }
        }
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

    public void setDeckSize(int newSize){
        this.deckSize = newSize;
    }
}