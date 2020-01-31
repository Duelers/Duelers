package models.game;


import models.card.Deck;

public class Story {
    private Deck deck;
    private GameType gameType;
    private int reward;

    public Story(Deck deck, GameType gameType, int reward) {
        this.deck = deck;
        this.gameType = gameType;
        this.reward = reward;
    }

    public Deck getDeck() {
        return deck;
    }

    public GameType getGameType() {
        return gameType;
    }

    int getReward() {
        return reward;
    }
}