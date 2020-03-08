package org.projectcardboard.client.models.compresseddata;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.projectcardboard.client.view.battleview.Constants;

import shared.models.card.Card;
import shared.models.card.CardType;
import shared.models.game.Troop;

public class CompressedPlayer {
    private String userName;
    private int currentMP;
    private List<Card> hand;
    private List<Card> graveyard;
    private Card nextCard;
    private int playerNumber;
    private List<Troop> troops;
    private Troop hero;

    private PropertyChangeSupport support = new PropertyChangeSupport(this);

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

    public void replaceNextCard(CompressedCard compressedCard) {
        if (nextCard == null)
            System.out.println("Client Game Error! replaceNextCard: Attempting to set nextCard but nextCard is null");
        else {
            nextCard = compressedCard;
            if (support == null) {
                support = new PropertyChangeSupport(this);
            }
            support.firePropertyChange("next", null, null);
        }
    }

    void addCardToGraveYard(Card card) {
        graveyard.add(card);
    }

    void troopUpdate(Troop troop) {
        if (troops == null)
            troops = new ArrayList<>();
        removeTroop(troop.getCard().getCardId());
        if (troop.getCurrentHp() > 0) {
            troops.add(troop);
            if (troop.getCard().getType() == CardType.HERO)
                hero = troop;
        }
    }

    void removeCardFromHand(String cardId) {
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

    void removeTroop(String cardId) {
        if (troops == null)
            troops = new ArrayList<>();
        troops.removeIf(troop -> troop.getCard().getCardId().equalsIgnoreCase(cardId));
        if (hero != null && hero.getCard().getCardId().equalsIgnoreCase(cardId))
            hero = null;
    }

    public List<Troop> getTroops() {
        return Collections.unmodifiableList(troops);
    }

    public void setTroops(List<Troop> troops) {
        this.troops = troops;

        for (Troop troop : troops) {
            if (troop.getCard().getType() == CardType.HERO) {
                hero = troop;
            }
        }
    }

    public Troop getHero() {
        return hero;
    }

    public Card searchGraveyard(String cardId) {
        for (Card card : graveyard) {
            if (card.getCardId().equalsIgnoreCase(cardId)) {
                return card;
            }
        }
        return null;
    }

    public String getUserName() {
        return userName;
    }

    public int getCurrentMP() {
        return currentMP;
    }

    void setCurrentMP(int currentMP, int turnNumber) {
        this.currentMP = currentMP;
    }

    public List<Card> getHand() {
        return Collections.unmodifiableList(hand);
    }

    public List<Card> getGraveyard() {
        return Collections.unmodifiableList(graveyard);
    }

    public Card getNextCard() {
        return nextCard;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }


}