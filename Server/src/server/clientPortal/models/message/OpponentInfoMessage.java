package server.clientPortal.models.message;

import server.dataCenter.models.account.Account;
import server.dataCenter.models.account.AccountInfo;

class OpponentInfoMessage {
    private AccountInfo opponentInfo;

    OpponentInfoMessage(Account opponent) {
        this.opponentInfo = new AccountInfo(opponent);
    }
}
