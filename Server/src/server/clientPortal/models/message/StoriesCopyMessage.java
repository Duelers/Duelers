package server.clientPortal.models.message;

import server.dataCenter.models.card.spell.DeckInfo;
import server.gameCenter.models.game.Story;

class StoriesCopyMessage {//TODO:send reward and game type
    private DeckInfo[] stories;

    StoriesCopyMessage(Story[] stories) {
        this.stories = new DeckInfo[stories.length];//TODO:reCode Story
        for (int i = 0; i < stories.length; i++) {
            this.stories[i] = new DeckInfo(stories[i]);
        }
    }
}
