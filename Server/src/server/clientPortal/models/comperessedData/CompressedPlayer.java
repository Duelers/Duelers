package server.clientPortal.models.comperessedData;


import server.dataCenter.models.card.ServerCard;
import server.gameCenter.models.game.ServerTroop;
import shared.models.game.BasePlayer;

import java.util.List;

public class CompressedPlayer extends BasePlayer<ServerCard, ServerTroop> {
    public CompressedPlayer(String userName,
                            int currentMP,
                            List<ServerCard> hand, List<ServerCard> graveyard,
                            ServerCard nextCard, int playerNumber,
                            List<ServerTroop> troops, ServerTroop hero) {
        super(userName, currentMP, hand, graveyard, nextCard, playerNumber, troops, hero);
    }
}
