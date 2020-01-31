package view;

import controller.StoryMenuController;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import models.card.DeckInfo;
import models.gui.PlayButtonItem;

import java.io.FileNotFoundException;

class StoryMenu extends PlayMenu {
    private static final String BACKGROUND_URL = "Client/resources/menu/background/story_background.jpg";
    private static final DeckInfo[] stories;
    private static final EventHandler<? super MouseEvent> BACK_EVENT = event -> SinglePlayerMenu.getInstance().show();
    private static final PlayButtonItem[] items;
    private static StoryMenu menu;

    static {
        synchronized (StoryMenuController.getInstance()) {
            if (StoryMenuController.getInstance().getStories() == null) {
                try {
                    StoryMenuController.getInstance().wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        stories = StoryMenuController.getInstance().getStories();

        items = new PlayButtonItem[]{
                new PlayButtonItem("Client/resources/menu/playButtons/kill_hero.jpg", "FIRST STAGE",
                        "Hero: " + stories[0].getHeroName() + ". Mode: Kill Hero",
                        event -> StoryMenuController.getInstance().startGame(0)
                ),
                new PlayButtonItem("Client/resources/menu/playButtons/single_flag.jpg", "SECOND STAGE",
                        "Hero: " + stories[1].getHeroName() + ". Mode: Single Flag",
                        event -> StoryMenuController.getInstance().startGame(1)
                ),
                new PlayButtonItem("Client/resources/menu/playButtons/multi_flag.jpg", "THIRD STAGE",
                        "Hero: " + stories[2].getHeroName() + ". Mode: Multi Flag",
                        event -> StoryMenuController.getInstance().startGame(2)
                )
        };
    }

    private StoryMenu() throws FileNotFoundException {
        super(items, BACKGROUND_URL, BACK_EVENT);
    }

    public static StoryMenu getInstance() {
        if (menu == null) {
            try {
                menu = new StoryMenu();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return menu;
    }
}