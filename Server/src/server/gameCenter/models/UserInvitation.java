package server.gameCenter.models;

import server.dataCenter.models.account.Account;
import shared.models.game.GameType;

public class UserInvitation {
    private final Account inviter;
    private final Account invited;
    private final GameType gameType;

    public UserInvitation(Account inviter, Account invited, GameType gameType) {
        this.inviter = inviter;
        this.invited = invited;
        this.gameType = gameType;
    }

    public Account getInviter() {
        return inviter;
    }

    public Account getInvited() {
        return invited;
    }

    public GameType getGameType() {
        return gameType;
    }
}
