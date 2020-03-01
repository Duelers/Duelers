package server.clientPortal.models.comperessedData;

import shared.models.card.Card;

import java.util.ArrayList;
import java.util.List;

public class CompressedPlayer {
    private String userName;
    private int currentMP;
    private ArrayList<Card> hand = new ArrayList<>();
    private ArrayList<Card> graveyard = new ArrayList<>();
    private Card nextCard;
    private int playerNumber;

    public CompressedPlayer(String userName, int currentMP, List<Card> hand, List<Card> graveyard,
                            Card nextCard, int playerNumber) {
        this.userName = userName;
        this.currentMP = currentMP;
        this.hand.addAll(hand);
        this.graveyard.addAll(graveyard);
        this.nextCard = nextCard;
        this.playerNumber = playerNumber;
    }
}
