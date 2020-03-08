package server.dataCenter.models.account;

import server.gameCenter.models.game.Player;

import java.util.Date;

public class MatchHistory {
    private final String oppName;
    private final boolean amIWinner;
    private final Date date;

    public MatchHistory(Player player, boolean amIWinner) {
        if (player.getUserName().equals("AI")) {
            this.oppName = player.getDeck().getDeckName();
        } else {
            this.oppName = player.getUserName();
        }
        this.amIWinner = amIWinner;
        this.date = new Date(System.currentTimeMillis());
    }

    public String getOppName() {
        return this.oppName;
    }

    public String getDate() {
        return date.toString();
    }

    public boolean isAmIWinner() {
        return amIWinner;
    }
}