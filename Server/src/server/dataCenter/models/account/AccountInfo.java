package server.dataCenter.models.account;

import server.dataCenter.DataCenter;

public class AccountInfo {
    private final String username;
    private final boolean online;
    private final int wins;
    private final AccountType type;

    public AccountInfo(Account account) {
        this.username = account.getUsername();
        this.online = DataCenter.getInstance().isOnline(username);
        this.wins = account.getWins();
        this.type = account.getAccountType();
    }
}
