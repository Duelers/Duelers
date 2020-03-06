package server.clientPortal.models.message;

import server.dataCenter.models.account.Account;
import server.dataCenter.models.account.TempAccount;

public class AccountCopyMessage {

    public AccountCopyMessage(Account account) {
        TempAccount account1 = new TempAccount(account);
    }
}
