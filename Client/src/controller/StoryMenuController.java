package controller;

import models.Constants;
import models.card.DeckInfo;
import models.message.DataName;
import models.message.Message;

public class StoryMenuController {
    private static StoryMenuController ourInstance;
    private DeckInfo[] stories = null;

    private StoryMenuController() {
    }

    public static StoryMenuController getInstance() {
        if (ourInstance == null) {
            ourInstance = new StoryMenuController();
            Client.getInstance().addToSendingMessagesAndSend(
                    Message.makeGetDataMessage(Constants.SERVER_NAME, DataName.STORIES));
        }
        return ourInstance;
    }

    public void startGame(int stage) {
        Client.getInstance().addToSendingMessagesAndSend(
                Message.makeNewStoryGameMessage(Constants.SERVER_NAME, stage));
    }

    public DeckInfo[] getStories() {
        return stories;
    }

    synchronized void setStories(DeckInfo[] stories) {
        this.stories = stories;
        this.notify();
    }
}
