package message;

import localDataCenter.models.account.Account;
import localDataCenter.models.account.TempAccount;

public class AccountCopyMessage {
    private TempAccount account;

    public TempAccount getAccount() {
        return account;
    }

    public AccountCopyMessage(Account account) {
        this.account = new TempAccount(account);
    }
}
