package server.dataCenter.models.account;

import server.dataCenter.DataCenter;

public class AccountInfo {

    public AccountInfo(Account account) {
        String username = account.getUsername();
        boolean online = DataCenter.getInstance().isOnline(username);
        int wins = account.getWins();
        AccountType type = account.getAccountType();
    }
}
