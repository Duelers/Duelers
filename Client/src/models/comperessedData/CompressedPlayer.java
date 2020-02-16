package models.comperessedData;

import models.card.CardType;
import view.BattleView.Constants;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CompressedPlayer {
    private String userName;
    private int currentMP;
    private List<CompressedCard> hand;
    private List<CompressedCard> graveyard;
    private CompressedCard nextCard;
    private CompressedCard usableItem;
    private List<CompressedCard> collectedItems;
    private int playerNumber;
    private int numberOfCollectedFlags;
    private List<CompressedTroop> troops;
    private CompressedTroop hero;

    private PropertyChangeSupport support = new PropertyChangeSupport(this);

    //just for testing BattleView
    public CompressedPlayer(String userName, int currentMP, ArrayList<CompressedCard> hand,
                            ArrayList<CompressedCard> graveyard, CompressedCard nextCard,
                            ArrayList<CompressedCard> collectedItems, int playerNumber, int numberOfCollectedFlags,
                            ArrayList<CompressedTroop> troops, CompressedTroop hero) {
        this.userName = userName;
        this.currentMP = currentMP;
        this.hand = hand;
        this.graveyard = graveyard;
        this.nextCard = nextCard;
        this.collectedItems = collectedItems;
        this.playerNumber = playerNumber;
        this.numberOfCollectedFlags = numberOfCollectedFlags;
        this.troops = troops;
        this.hero = hero;
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

    public void addCardToNext(CompressedCard card) {
        if (nextCard != null)
            System.out.println("Client Game Error!");
        else {
            nextCard = card;
            if (support == null) {
                support = new PropertyChangeSupport(this);
            }
            support.firePropertyChange("next", null, null);
        }
    }

    public void replaceSelectedCard(int selectedCardIndex){
        hand.set(selectedCardIndex, nextCard);
        removeCardFromNext();
        if(support == null){
            support = new PropertyChangeSupport(this);
        }
        support.firePropertyChange("replace",null,null);
    }

    void addCardToCollectedItems(CompressedCard card) {
        collectedItems.add(card);
        if (support == null) {
            support = new PropertyChangeSupport(this);
        }
        support.firePropertyChange("items", null, null);
    }

    void addCardToGraveYard(CompressedCard card) {
        graveyard.add(card);
    }

    void troopUpdate(CompressedTroop troop) {
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
        hand.removeIf(compressedCard -> compressedCard.getCardId().equalsIgnoreCase(cardId));
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

    void removeCardFromCollectedItems(String cardId) {
        collectedItems.removeIf(compressedCard -> compressedCard.getCardId().equalsIgnoreCase(cardId));
        if (support == null) {
            support = new PropertyChangeSupport(this);
        }
        support.firePropertyChange("items", null, null);
    }

    void removeTroop(String cardId) {
        if (troops == null)
            troops = new ArrayList<>();
        troops.removeIf(compressedTroop -> compressedTroop.getCard().getCardId().equalsIgnoreCase(cardId));
        if (hero != null && hero.getCard().getCardId().equalsIgnoreCase(cardId))
            hero = null;
    }

    public List<CompressedTroop> getTroops() {
        return Collections.unmodifiableList(troops);
    }

    public void setTroops(List<CompressedTroop> troops) {
        this.troops = troops;

        for (CompressedTroop troop : troops) {
            if (troop.getCard().getType() == CardType.HERO) {
                hero = troop;
            }
        }
    }

    public CompressedTroop getHero() {
        return hero;
    }

    public CompressedCard searchGraveyard(String cardId) {
        for (CompressedCard card : graveyard) {
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

    public List<CompressedCard> getHand() {
        return Collections.unmodifiableList(hand);
    }

    public List<CompressedCard> getGraveyard() {
        return Collections.unmodifiableList(graveyard);
    }

    public CompressedCard getNextCard() {
        return nextCard;
    }

    public List<CompressedCard> getCollectedItems() {
        return Collections.unmodifiableList(collectedItems);
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public int getNumberOfCollectedFlags() {
        return numberOfCollectedFlags;
    }

    void setNumberOfCollectedFlags(int numberOfCollectedFlags) {
        this.numberOfCollectedFlags = numberOfCollectedFlags;
        //TODO:support.firePropertyChange("troop", getTroop(troop.getCard().getCardId()), troop);
    }

    public List<CompressedTroop> getFlagCarriers() {
        ArrayList<CompressedTroop> flagCarriers = new ArrayList<>();

        for (CompressedTroop troop : troops) {
            if (troop.getNumberOfCollectedFlags() > 0) {
                flagCarriers.add(troop);
            }
        }

        return Collections.unmodifiableList(flagCarriers);
    }

    public CompressedCard getUsableItem() {
        return usableItem;
    }
}
