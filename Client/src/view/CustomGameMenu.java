package view;

import controller.Client;
import controller.CustomGameMenuController;
import models.card.Deck;
import models.game.GameType;
import models.gui.*;

import java.io.FileNotFoundException;
import java.util.List;

class CustomGameMenu extends GameModeChooseMenu {
    private static final String BACKGROUND_URL = "Client/resources/menu/background/custom_game_background.jpg";
    private static CustomGameMenu menu;

    private String[] deckNames;

    private CustomGameMenu() throws FileNotFoundException {
        super(BACKGROUND_URL);
        initializeDecks();
    }

    public static CustomGameMenu getInstance() {
        if (menu == null) {
            try {
                menu = new CustomGameMenu();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return menu;
    }

    private void initializeDecks() {
        List<Deck> decks = Client.getInstance().getAccount().getDecks();
        deckNames = new String[decks.size()];
        for (int i = 0; i < deckNames.length; i++) {
            deckNames[i] = decks.get(i).getName();
        }
    }

    @Override
    void startKillHero() {
        DialogText text = new DialogText("Please choose one of your decks to be as opponent's deck");
        DeckListView listView = new DeckListView(deckNames);
        DialogBox dialogBox = new DialogBox(text, listView);
        DialogContainer dialogContainer = new DialogContainer(root, dialogBox);

        dialogBox.makeButton("START", buttonEvent -> {
            if (listView.getSelectionModel().getSelectedItem() == null) return;
            CustomGameMenuController.getInstance().startGame(
                    listView.getSelectionModel().getSelectedItem(), GameType.KILL_HERO
            );
            dialogContainer.close();
        });
        dialogContainer.show();
        dialogBox.makeClosable(dialogContainer);
    }

}
