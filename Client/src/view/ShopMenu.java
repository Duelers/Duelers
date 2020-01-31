package view;

import controller.GraphicalUserInterface;
import controller.ShopController;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import models.account.Collection;
import models.gui.*;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileNotFoundException;

import static models.gui.UIConstants.*;

public class ShopMenu extends Show implements PropertyChangeListener {
    private static final Font TITLE_FONT = Font.font("DejaVu Sans Light", FontWeight.EXTRA_LIGHT, 45 * SCALE);
    private static final double SCROLL_WIDTH = 2350 * SCALE;
    private static final double SCROLL_HEIGHT = SCENE_HEIGHT - DEFAULT_SPACING * 13;
    private static Media backgroundMusic = new Media(
            new File("Client/resources/music/shop_menu.m4a").toURI().toString()
    );
    private static ShopMenu menu;
    private static final EventHandler<? super MouseEvent> BACK_EVENT = event -> {
        ShopController.getInstance().removePropertyChangeListener(menu);
        menu.searchBox.clear();
        new MainMenu().show();
    };
    private ShopSearchBox searchBox;
    private VBox cardsBox;
    private Collection showingCards;

    ShopMenu() {
        menu = this;
        setOriginalCards();

        try {
            root.setBackground(UIConstants.DEFAULT_ROOT_BACKGROUND);

            BorderPane background = BackgroundMaker.getMenuBackground();
            BackButton backButton = new BackButton(BACK_EVENT);

            searchBox = new ShopSearchBox();
            ScrollPane cardsScroll = makeCardsScroll();

            VBox shopPane = makeShopPane(searchBox, cardsScroll);

            AnchorPane sceneContents = new AnchorPane(background, shopPane, backButton);
            showGlobalChatDialog(sceneContents);
            root.getChildren().addAll(sceneContents);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void showGlobalChatDialog(AnchorPane sceneContents) {
        sceneContents.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.T)) {
                GlobalChatDialog.getInstance().show();
            }
        });
    }

    private ScrollPane makeCardsScroll() throws FileNotFoundException {
        makeCardGrids();
        ScrollPane cardsScroll = new ScrollPane(cardsBox);
        cardsScroll.setMinSize(SCROLL_WIDTH, SCROLL_HEIGHT);
        cardsScroll.setMaxWidth(SCROLL_WIDTH);
        cardsScroll.setId("background_transparent");
        return cardsScroll;
    }

    private void makeCardGrids() throws FileNotFoundException {
        DefaultLabel heroesLabel = new DefaultLabel("HEROES", TITLE_FONT, Color.WHITE);
        ShopCardsGrid heroesGrid = new ShopCardsGrid(showingCards.getHeroes());

        DefaultLabel minionsLabel = new DefaultLabel("MINIONS", TITLE_FONT, Color.WHITE);
        ShopCardsGrid minionsGrid = new ShopCardsGrid(showingCards.getMinions());

        DefaultLabel spellsLabel = new DefaultLabel("SPELLS", TITLE_FONT, Color.WHITE);
        ShopCardsGrid spellsGrid = new ShopCardsGrid(showingCards.getSpells());

        DefaultLabel itemsLabel = new DefaultLabel("ITEMS", TITLE_FONT, Color.WHITE);
        ShopCardsGrid itemsGrid = new ShopCardsGrid(showingCards.getItems());

        cardsBox = new VBox(UIConstants.DEFAULT_SPACING * 4,
                heroesLabel, heroesGrid, minionsLabel, minionsGrid, spellsLabel, spellsGrid, itemsLabel, itemsGrid
        );
        cardsBox.setMinSize(SCROLL_WIDTH * 0.95, SCROLL_HEIGHT * 0.95);
        cardsBox.setAlignment(Pos.TOP_CENTER);
    }

    private VBox makeShopPane(ShopSearchBox searchBox, ScrollPane cardsScroll) {
        VBox shopPane = new VBox(UIConstants.DEFAULT_SPACING * 4, searchBox, cardsScroll);
        shopPane.setPadding(new Insets(UIConstants.DEFAULT_SPACING * 3));
        shopPane.setAlignment(Pos.CENTER);
        shopPane.setMinSize(UIConstants.SCENE_WIDTH, UIConstants.SCENE_HEIGHT);
        shopPane.setMaxSize(UIConstants.SCENE_WIDTH, UIConstants.SCENE_HEIGHT);
        return shopPane;
    }

    private void setOriginalCards() {
        ShopController.getInstance().addPropertyChangeListener(this);
        synchronized (ShopController.getInstance()) {
            if (ShopController.getInstance().getShowingCards() == null) {
                try {
                    ShopController.getInstance().wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        showingCards = ShopController.getInstance().getShowingCards();
    }

    @Override
    public void show() {
        super.show();
        BackgroundMaker.makeMenuBackgroundFrozen();
        GraphicalUserInterface.getInstance().setBackgroundMusic(backgroundMusic);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("search_result")) {
            showingCards = (Collection) evt.getNewValue();
            Platform.runLater(() -> {
                try {
                    cardsBox.getChildren().set(1, new ShopCardsGrid(showingCards.getHeroes()));
                    cardsBox.getChildren().set(3, new ShopCardsGrid(showingCards.getMinions()));
                    cardsBox.getChildren().set(5, new ShopCardsGrid(showingCards.getSpells()));
                    cardsBox.getChildren().set(7, new ShopCardsGrid(showingCards.getItems()));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public void search() {
        searchBox.search();
    }
}
