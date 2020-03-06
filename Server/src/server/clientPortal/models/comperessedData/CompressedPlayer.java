package server.clientPortal.models.comperessedData;

import shared.models.card.Card;

import java.util.ArrayList;
import java.util.List;

public class CompressedPlayer {

    public CompressedPlayer(String userName, int currentMP, List<Card> hand, List<Card> graveyard,
                            Card nextCard, int playerNumber) {
        ArrayList<Card> hand1 = new ArrayList<>();
        hand1.addAll(hand);
        ArrayList<Card> graveyard1 = new ArrayList<>();
        graveyard1.addAll(graveyard);
    }
}
