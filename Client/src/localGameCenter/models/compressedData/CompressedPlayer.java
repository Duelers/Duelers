package localGameCenter.models.compressedData;

import localDataCenter.models.card.Card;
import localGameCenter.models.compressedData.CompressedCard;

import java.util.ArrayList;
import java.util.List;

public class CompressedPlayer {
    private String userName;
    private int currentMP;
    private ArrayList<CompressedCard> hand = new ArrayList<>();
    private ArrayList<CompressedCard> graveyard = new ArrayList<>();
    private CompressedCard nextCard;
    private int playerNumber;

    public CompressedPlayer(String userName, int currentMP, List<Card> hand, List<Card> graveyard,
                            Card nextCard, int playerNumber) {
        this.userName = userName;
        this.currentMP = currentMP;
        for (Card card : hand)
            this.hand.add(card.toCompressedCard());
        for (Card card : graveyard)
            this.graveyard.add(card.toCompressedCard());
        this.nextCard = nextCard.toCompressedCard();
        this.playerNumber = playerNumber;
    }
}
