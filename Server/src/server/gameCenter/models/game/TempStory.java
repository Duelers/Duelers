package server.gameCenter.models.game;

import server.dataCenter.models.card.TempDeck;

public class TempStory {
    private TempDeck deck;
    private GameType gameType;
    private int reward;

    public TempStory(Story story) {
        this.deck = new TempDeck(story.getDeck());
        this.gameType = story.getGameType();
        this.reward = story.getReward();
    }

    public TempDeck getDeck() {
        return deck;
    }

    public GameType getGameType() {
        return gameType;
    }

    public int getReward() {
        return reward;
    }
}
