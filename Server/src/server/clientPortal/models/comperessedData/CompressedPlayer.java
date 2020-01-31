package server.clientPortal.models.comperessedData;

import server.dataCenter.models.card.Card;

import java.util.ArrayList;
import java.util.List;

public class CompressedPlayer {
    private String userName;
    private int currentMP;
    private ArrayList<CompressedCard> hand = new ArrayList<>();
    private ArrayList<CompressedCard> graveyard = new ArrayList<>();
    private CompressedCard usableItem;
    private CompressedCard nextCard;
    private ArrayList<CompressedCard> collectedItems = new ArrayList<>();
    private int playerNumber;
    private int numberOfCollectedFlags;

    public CompressedPlayer(String userName, int currentMP, List<Card> hand, List<Card> graveyard,
                            Card nextCard, CompressedCard usableItem, List<Card> collectedItems, int playerNumber, int numberOfCollectedFlags) {
        this.userName = userName;
        this.currentMP = currentMP;
        this.usableItem = usableItem;
        for (Card card : hand)
            this.hand.add(card.toCompressedCard());
        for (Card card : graveyard)
            this.graveyard.add(card.toCompressedCard());
        this.nextCard = nextCard.toCompressedCard();
        for (Card card : collectedItems)
            this.collectedItems.add(card.toCompressedCard());
        this.playerNumber = playerNumber;
        this.numberOfCollectedFlags = numberOfCollectedFlags;
    }
}
