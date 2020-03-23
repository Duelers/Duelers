package server.dataCenter.models.account;

import server.gameCenter.models.game.Player;
import shared.models.account.BaseMatchHistory;

import java.util.Date;

public class MatchHistory extends BaseMatchHistory {
    public MatchHistory(Player player, boolean amIWinner) { //Sorry this is ugly. It has to be inline since super has to be the first call.
        super("AI".equals(player.getUserName()) ?
                        player.getDeck().getName() : player.getUserName(),
                amIWinner);
    }
}