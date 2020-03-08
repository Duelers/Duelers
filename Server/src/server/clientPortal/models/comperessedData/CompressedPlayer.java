package server.clientPortal.models.comperessedData;


import server.dataCenter.models.card.ServerCard;

import java.util.ArrayList;
import java.util.List;

public class CompressedPlayer {
    private final String userName;
    private final int currentMP;
    private final ArrayList<ServerCard> hand = new ArrayList<>();
    private final ArrayList<ServerCard> graveyard = new ArrayList<>();
    private final ServerCard nextCard;
    private final int playerNumber;

    public CompressedPlayer(String userName, int currentMP, List<ServerCard> hand, List<ServerCard> graveyard,
                            ServerCard nextCard, int playerNumber) {
        this.userName = userName;
        this.currentMP = currentMP;
        this.hand.addAll(hand);
        this.graveyard.addAll(graveyard);
        this.nextCard = nextCard;
        this.playerNumber = playerNumber;
    }
}
