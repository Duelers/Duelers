package message;

import localGameCenter.models.game.GameType;

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

    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }

    public GameType getGameType() {
        return gameType;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }

    public int getStage() {
        return stage;
    }

    public void setCustomDeckName(String customDeckName) {
        this.customDeckName = customDeckName;
    }

    public String getCustomDeckName() {
        return customDeckName;
    }

    public void setOpponentUsername(String opponentUsername) {
        this.opponentUsername = opponentUsername;
    }

    public String getOpponentUsername() {
        return opponentUsername;
    }
}
