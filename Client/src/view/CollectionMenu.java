package view;

import com.google.gson.Gson;
import controller.Client;
import controller.CollectionMenuController;
import controller.GraphicalUserInterface;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import models.Constants;
import models.account.Collection;
import models.card.Deck;
import models.card.ExportedDeck;
import models.gui.*;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;

import static models.gui.UIConstants.*;

public class CollectionMenu extends Show implements PropertyChangeListener {
    private static final Background DECKS_BACKGROUND = new Background(
            new BackgroundFill(Color.rgb(39, 35, 40), CornerRadii.EMPTY, Insets.EMPTY)
    );
    private static final Font TITLE_FONT = Font.font("DejaVu Sans Light", FontWeight.EXTRA_LIGHT, 45 * SCALE);
    private static final double COLLECTION_WIDTH = SCENE_WIDTH * 0.8;
    private static final double DECKS_WIDTH = SCENE_WIDTH * 0.2;
    private static final double SCROLL_HEIGHT = SCENE_HEIGHT - DEFAULT_SPACING * 13;
    private static final Insets DECKS_PADDING = new Insets(20 * SCALE, 5 * SCALE, 0, 40 * SCALE);
    private static CollectionMenu menu;
    private static final EventHandler<? super MouseEvent> BACK_EVENT = event -> {
        Client.getInstance().getAccount().removePropertyChangeListener(menu);
        CollectionMenuController.getInstance().removePropertyChangeListener(menu);
        menu.searchBox.clear();
        new MainMenu().show();
    };
    private static Media backgroundMusic = new Media(
            new File("Client/resources/music/collection_menu.m4a").toURI().toString()
    );
    private VBox collectionBox;
    private ImageButton showCollectionButton;
    private CollectionSearchBox searchBox;
    private VBox cardsBox;
    private VBox decksBox;
    private Collection showingCards;

    CollectionMenu() {
        menu = this;
        setCollectionCards();

        try {
            root.setBackground(DEFAULT_ROOT_BACKGROUND);

            BorderPane background = BackgroundMaker.getMenuBackground();
            BackButton backButton = new BackButton(BACK_EVENT);

            HBox collectionPane = new HBox();

            VBox decksPane = new VBox(DEFAULT_SPACING);
            decksPane.setPadding(DECKS_PADDING);
            decksPane.setMinSize(DECKS_WIDTH, SCENE_HEIGHT);
            decksPane.setAlignment(Pos.TOP_CENTER);
            decksPane.setBackground(DECKS_BACKGROUND);

            StackPane newDeckButton = new ImageButton("NEW DECK", event -> showNewDeckDialog());
            StackPane importDeckButton = new ImageButton("IMPORT DECK", event -> importDeck());

            decksBox = new VBox(DEFAULT_SPACING);
            decksBox.setAlignment(Pos.TOP_CENTER);

            for (Deck deck : Client.getInstance().getAccount().getDecks()) {
                decksBox.getChildren().addAll(new DeckBox(deck));
            }

            ScrollPane decksScroll = new ScrollPane(decksBox);
            decksScroll.setId("background_transparent");
            decksScroll.setMaxHeight(SCENE_HEIGHT * 0.86);

            decksPane.getChildren().addAll(newDeckButton, importDeckButton, decksScroll);

            collectionBox = new VBox(DEFAULT_SPACING * 4);
            collectionBox.setPadding(new Insets(DEFAULT_SPACING * 3));
            collectionBox.setAlignment(Pos.CENTER);
            collectionBox.setMinSize(COLLECTION_WIDTH, SCENE_HEIGHT);
            collectionBox.setMaxSize(COLLECTION_WIDTH, SCENE_HEIGHT);

            searchBox = new CollectionSearchBox();
            cardsBox = new VBox(DEFAULT_SPACING * 4);
            showCollectionCards();

            ScrollPane cardsScroll = new ScrollPane(cardsBox);
            cardsScroll.setMinWidth(COLLECTION_WIDTH);
            cardsScroll.setMaxWidth(COLLECTION_WIDTH);
            cardsScroll.setId("background_transparent");

            showCollectionButton = new ImageButton("BACK", event -> {
                try {
                    showCollectionCards();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            });

            collectionBox.getChildren().addAll(searchBox, cardsScroll);

            collectionPane.getChildren().addAll(collectionBox, decksPane);

            AnchorPane sceneContents = new AnchorPane(background, collectionPane, backButton);

            showGlobalChatDialog(sceneContents);

            root.getChildren().addAll(sceneContents);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static CollectionMenu getInstance() {
        return menu;
    }

    private void showGlobalChatDialog(AnchorPane sceneContents) {
        sceneContents.setOnKeyPressed(event -> {
            if (event.getCode().equals(Constants.KEY_FOR_CHAT)) {
                GlobalChatDialog.getInstance().show();
            }
        });
    }

    private void importDeck() {
        String deckJson = showJFileChooserDialog();
        if (deckJson != null && !deckJson.isEmpty())
            CollectionMenuController.getInstance().importDeck(new Gson().fromJson(deckJson, ExportedDeck.class));
    }

    private String showJFileChooserDialog() {
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

        GraphicalUserInterface.getInstance().closeFullscreen();

        jfc.setAcceptAllFileFilterUsed(true);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG and GIF images", "json");
        jfc.addChoosableFileFilter(filter);
        int returnValue = jfc.showOpenDialog(null);
        GraphicalUserInterface.getInstance().makeFullScreen();
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = jfc.getSelectedFile();
            System.out.println(selectedFile.getAbsolutePath());
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(selectedFile)));
                return bufferedReader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void showCollectionCards() throws FileNotFoundException {
        searchBox.setVisible(true);
        collectionBox.getChildren().remove(showCollectionButton);
        cardsBox.getChildren().clear();

        DefaultLabel heroesLabel = new DefaultLabel("HEROES", TITLE_FONT, Color.WHITE);
        CollectionCardsGrid heroesGrid = new CollectionCardsGrid(showingCards.getHeroes());

        DefaultLabel minionsLabel = new DefaultLabel("MINIONS", TITLE_FONT, Color.WHITE);
        CollectionCardsGrid minionsGrid = new CollectionCardsGrid(showingCards.getMinions());

        DefaultLabel spellsLabel = new DefaultLabel("SPELLS", TITLE_FONT, Color.WHITE);
        CollectionCardsGrid spellsGrid = new CollectionCardsGrid(showingCards.getSpells());

        DefaultLabel itemsLabel = new DefaultLabel("ITEMS", TITLE_FONT, Color.WHITE);
        CollectionCardsGrid itemsGrid = new CollectionCardsGrid(showingCards.getItems());

        cardsBox.getChildren().addAll(
                heroesLabel, heroesGrid, minionsLabel, minionsGrid, spellsLabel, spellsGrid, itemsLabel, itemsGrid
        );
        cardsBox.setMinSize(COLLECTION_WIDTH * 0.95, SCROLL_HEIGHT * 0.95);
        cardsBox.setAlignment(Pos.TOP_CENTER);
    }

    private void showNewDeckDialog() {
        DialogText name = new DialogText("Please enter deck's name");
        NormalField nameField = new NormalField("deck name");
        DialogBox dialogBox = new DialogBox(name, nameField);
        DialogContainer dialogContainer = new DialogContainer(root, dialogBox);

        dialogBox.makeButton("CREATE", buttonEvent -> {
            if (nameField.getText().equals("")) return;
            CollectionMenuController.getInstance().newDeck(nameField.getText());
            dialogContainer.close();
        });

        dialogContainer.show();
        dialogBox.makeClosable(dialogContainer);
    }

    private void setCollectionCards() {
        Client.getInstance().getAccount().addPropertyChangeListener(this);
        CollectionMenuController.getInstance().addPropertyChangeListener(this);
        showingCards = CollectionMenuController.getInstance().getCurrentShowingCards();
    }

    @Override
    public void show() {
        super.show();
        Client.getInstance().getAccount().addPropertyChangeListener(this);
        BackgroundMaker.makeMenuBackgroundFrozen();
        GraphicalUserInterface.getInstance().setBackgroundMusic(backgroundMusic);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("search_result")) {
            showingCards = (Collection) evt.getNewValue();
            Platform.runLater(() -> {
                try {
                    cardsBox.getChildren().set(1, new CollectionCardsGrid(showingCards.getHeroes()));
                    cardsBox.getChildren().set(3, new CollectionCardsGrid(showingCards.getMinions()));
                    cardsBox.getChildren().set(5, new CollectionCardsGrid(showingCards.getSpells()));
                    cardsBox.getChildren().set(7, new CollectionCardsGrid(showingCards.getItems()));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            });

        }

        if (evt.getPropertyName().equals("decks") || evt.getPropertyName().equals("main_deck")) {
            Platform.runLater(() -> {
                decksBox.getChildren().clear();

                for (Deck deck : Client.getInstance().getAccount().getDecks()) {
                    decksBox.getChildren().add(new DeckBox(deck));
                }
            });
        }
    }

    public void modify(Deck deck) {
        try {
            cardsBox.getChildren().clear();
            collectionBox.getChildren().remove(showCollectionButton);
            collectionBox.getChildren().add(0, showCollectionButton);
            searchBox.setVisible(false);
            CollectionMenuController.getInstance().search("");

            DefaultLabel heroesLabel = new DefaultLabel("HEROES", TITLE_FONT, Color.WHITE);
            DeckCardsGrid heroesGrid = new DeckCardsGrid(showingCards.getHeroes(), deck);

            DefaultLabel minionsLabel = new DefaultLabel("MINIONS", TITLE_FONT, Color.WHITE);
            DeckCardsGrid minionsGrid = new DeckCardsGrid(showingCards.getMinions(), deck);

            DefaultLabel spellsLabel = new DefaultLabel("SPELLS", TITLE_FONT, Color.WHITE);
            DeckCardsGrid spellsGrid = new DeckCardsGrid(showingCards.getSpells(), deck);

            DefaultLabel itemsLabel = new DefaultLabel("ITEMS", TITLE_FONT, Color.WHITE);
            DeckCardsGrid itemsGrid = new DeckCardsGrid(showingCards.getItems(), deck);
            cardsBox.getChildren().addAll(
                    heroesLabel, heroesGrid, minionsLabel, minionsGrid, spellsLabel, spellsGrid, itemsLabel, itemsGrid
            );
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
