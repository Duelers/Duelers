package server.clientPortal.models.message;

import server.dataCenter.models.account.Account;
import server.dataCenter.models.account.AccountInfo;

class OpponentInfoMessage {

    OpponentInfoMessage(Account opponent) {
        AccountInfo opponentInfo = new AccountInfo(opponent);
    }
}
