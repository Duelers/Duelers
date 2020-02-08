package server.gameCenter.models.game;

import server.dataCenter.models.account.Collection;
import server.dataCenter.models.card.Deck;

public class Story {
    private Deck deck;
    private GameType gameType;
    private int reward;

    public Story(Deck deck, GameType gameType, int reward) {
        this.deck = deck;
        this.gameType = gameType;
        this.reward = reward;

    }

    public Story(TempStory story, Collection originalCards) {
        this.deck = new Deck(story.getDeck(), originalCards);
        deck.copyCards();
        this.gameType = story.getGameType();
        this.reward = story.getReward();
    }

    public Deck getDeck() {
        return deck;
    }

    public GameType getGameType() {
        return gameType;
    }

    public int getReward() {
        return reward;
    }
}