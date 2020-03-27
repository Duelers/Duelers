package shared.models.account;

public class BaseAccountInfo {
    private final String username;
    private final boolean online;
    private final int wins;
    protected AccountType type;


    public BaseAccountInfo(String username, boolean online, int wins, AccountType type) {
        this.username = username;
        this.online = online;
        this.wins = wins;
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public boolean isOnline() {
        return online;
    }

    public int getWins() {
        return wins;
    }

    public AccountType getType() {
        return type;
    }
}
