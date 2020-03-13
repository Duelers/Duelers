package shared.models.game;

import shared.models.card.Card;

import java.util.ArrayList;
import java.util.List;

public class BasePlayer<CardType extends Card> {
    private final String userName;
    private final int currentMP;
    private final ArrayList<CardType> hand = new ArrayList<>();
    private final ArrayList<CardType> graveyard = new ArrayList<>();
    private final CardType nextCard;
    private final int playerNumber;

    public BasePlayer(String userName, int currentMP,
                      List<CardType> hand, List<CardType> graveyard,
                      CardType nextCard, int playerNumber) {
        this.userName = userName;
        this.currentMP = currentMP;
        this.hand.addAll(hand);
        this.graveyard.addAll(graveyard);
        this.nextCard = nextCard;
        this.playerNumber = playerNumber;
    }

}
