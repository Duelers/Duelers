package server.gameCenter.models;

import server.dataCenter.models.account.Account;
import server.gameCenter.models.game.GameType;

public class GlobalRequest {
    private final Account requester;
    private final GameType gameType;
    private final int numberOfFlags;

    public GlobalRequest(Account requester, GameType gameType, int numberOfFlags) {
        this.requester = requester;
        this.gameType = gameType;
        this.numberOfFlags = numberOfFlags;
    }

    public Account getRequester() {
        return requester;
    }

    public GameType getGameType() {
        return gameType;
    }

    public int getNumberOfFlags() {
        return numberOfFlags;
    }
}
