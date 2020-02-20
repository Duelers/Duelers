package message;

import localDataCenter.models.account.Account;
import localDataCenter.models.account.AccountInfo;

public class OpponentInfoMessage {
    private AccountInfo opponentInfo;

    OpponentInfoMessage(Account opponent) {
        this.opponentInfo = new AccountInfo(opponent);
    }

    public AccountInfo getOpponentInfo() {
        return opponentInfo;
    }
}
