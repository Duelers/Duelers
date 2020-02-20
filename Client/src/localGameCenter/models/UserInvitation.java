package localGameCenter.models;

import localDataCenter.models.account.Account;
import localGameCenter.models.game.GameType;

public class UserInvitation {
    private final Account inviter;
    private final Account invited;
    private final GameType gameType;
    private final int numberOfFlags;

    public UserInvitation(Account inviter, Account invited, GameType gameType, int numberOfFlags) {
        this.inviter = inviter;
        this.invited = invited;
        this.gameType = gameType;
        this.numberOfFlags = numberOfFlags;
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

    public int getNumberOfFlags() {
        return numberOfFlags;
    }
}
