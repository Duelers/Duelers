package models.message;


import models.game.GameType;

public class NewGameFields {
    private GameType gameType;
    private int numberOfFlags;
    private int stage;
    private String customDeckName;
    private String opponentUsername;

    void setNumberOfFlags(int numberOfFlags) {
        this.numberOfFlags = numberOfFlags;
    }

    void setStage(int stage) {
        this.stage = stage;
    }

    void setCustomDeckName(String customDeckName) {
        this.customDeckName = customDeckName;
    }

    public GameType getGameType() {
        return gameType;
    }

    void setGameType(GameType gameType) {
        this.gameType = gameType;
    }

    public String getOpponentUsername() {
        return opponentUsername;
    }

    void setOpponentUsername(String opponentUsername) {
        this.opponentUsername = opponentUsername;
    }
}
