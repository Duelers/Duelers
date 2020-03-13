package server.clientPortal.models.comperessedData;


import server.dataCenter.models.card.ServerCard;
import shared.models.game.BasePlayer;

import java.util.List;

public class CompressedPlayer extends BasePlayer<ServerCard> {
    public CompressedPlayer(String userName, int currentMP, List<ServerCard> hand, List<ServerCard> graveyard,
                            ServerCard nextCard, int playerNumber) {
        super(userName, currentMP, hand, graveyard, nextCard, playerNumber);
    }
}
