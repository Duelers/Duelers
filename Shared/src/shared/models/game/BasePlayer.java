package shared.models.game;

import shared.models.card.Card;
import shared.models.game.map.Cell;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BasePlayer<
        CardType extends Card,
        TroopType extends Troop> {
    protected final String userName;
    protected int currentMP;

    protected TroopType hero;
    protected final ArrayList<CardType> hand = new ArrayList<>();
    protected List<TroopType> troops = new ArrayList<>();
    protected final ArrayList<CardType> graveyard = new ArrayList<>();
    protected CardType nextCard;
    protected final int playerNumber;

    public BasePlayer(String userName, int currentMP,
                      List<CardType> hand, List<CardType> graveyard, CardType nextCard,
                      int playerNumber,
                      List<TroopType> troops,
                      TroopType hero) {
        this.userName = userName;
        this.currentMP = currentMP;
        this.hand.addAll(hand);
        this.graveyard.addAll(graveyard);
        this.nextCard = nextCard;
        this.playerNumber = playerNumber;
        this.troops.addAll(troops);
        this.hero = hero;
    }

    public String getUserName() {
        return userName;
    }

    public int getCurrentMP() {
        return currentMP;
    }

    public List<CardType> getHand() {
        return Collections.unmodifiableList(hand);
    }

    public List<TroopType> getTroops() {
        return Collections.unmodifiableList(troops);
    }

    public TroopType getTroop(Cell cell) {
        for (TroopType troop : troops) {
            if (troop.getCell().equals(cell)) {
                return troop;
            }
        }
        return null;
    }

    public TroopType getTroop(String cardId) {
        for (TroopType troop : troops) {
            if (troop.getCard().getCardId().equalsIgnoreCase(cardId)) {
                return troop;
            }
        }
        return null;
    }

    public List<CardType> getGraveyard() {
        return Collections.unmodifiableList(graveyard);
    }

    public CardType getNextCard() {
        return nextCard;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public TroopType getHero() {
        return hero;
    }
}
