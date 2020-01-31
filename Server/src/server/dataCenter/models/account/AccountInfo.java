package server.dataCenter.models.account;

import server.dataCenter.DataCenter;

public class AccountInfo {
    private String username;
    private boolean online;
    private int wins;
    private AccountType type;

    public AccountInfo(Account account) {
        this.username = account.getUsername();
        this.online = DataCenter.getInstance().isOnline(username);
        this.wins = account.getWins();
        this.type = account.getAccountType();
    }
}
