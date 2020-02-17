package server.clientPortal.models.message;

import server.gameCenter.models.game.GameType;

public class NewGameFields {
    private GameType gameType;
    private int stage;
    private String customDeckName;
    private String opponentUsername;

    public NewGameFields(GameType gameType, int stage, String customDeckName, String opponentUsername) {
        this.gameType = gameType;
        this.stage = stage;
        this.customDeckName = customDeckName;
        this.opponentUsername = opponentUsername;
    }

    public GameType getGameType() {
        return gameType;
    }

    public int getStage() {
        return stage;
    }

    public String getCustomDeckName() {
        return customDeckName;
    }

    public String getOpponentUsername() {
        return opponentUsername;
    }
}
