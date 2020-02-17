package server.gameCenter.models;

import server.dataCenter.models.account.Account;
import server.gameCenter.models.game.GameType;

public class GlobalRequest {
    private final Account requester;
    private final GameType gameType;

    public GlobalRequest(Account requester, GameType gameType) {
        this.requester = requester;
        this.gameType = gameType;
    }

    public Account getRequester() {
        return requester;
    }

    public GameType getGameType() {
        return gameType;
    }

}
