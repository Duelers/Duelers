package models.message;


import shared.models.game.GameType;

public class NewGameFields {
    private GameType gameType;
    private String opponentUsername;

    void setStage(int stage) {
    }

    void setCustomDeckName(String customDeckName) {
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
