package server.clientPortal.models.message;

import server.dataCenter.models.account.Account;
import server.dataCenter.models.account.TempAccount;

public class AccountCopyMessage {
    private TempAccount account;

    public AccountCopyMessage(Account account) {
        this.account = new TempAccount(account);
    }
}
