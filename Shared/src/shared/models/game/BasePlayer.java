package shared.models.game;

import shared.models.card.Card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BasePlayer<
        CardType extends Card,
        TroopType extends Troop> {
    private final String userName;
    protected int currentMP;
    protected final ArrayList<CardType> hand = new ArrayList<>();
    protected final ArrayList<CardType> graveyard = new ArrayList<>();
    protected CardType nextCard;
    private final int playerNumber;
    protected List<TroopType> troops = new ArrayList<>();
    protected TroopType hero;

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

    public Troop getHero() {
        return hero;
    }
}
