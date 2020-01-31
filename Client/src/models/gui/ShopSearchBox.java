package models.gui;

import controller.Client;
import controller.ShopController;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileNotFoundException;
import java.util.concurrent.atomic.AtomicReference;

import static models.gui.UIConstants.SCALE;

public class ShopSearchBox extends HBox implements PropertyChangeListener {
    private static final double ICON_SIZE = 50 * SCALE;
    private static final String GOLD_ICON_URL = "Client/resources/ui/icon_gold.png";
    private final DefaultLabel money;
    private final SearchField searchField;

    public ShopSearchBox() throws FileNotFoundException {
        super(UIConstants.DEFAULT_SPACING * 2);
        Client.getInstance().getAccount().addPropertyChangeListener(this);
        setAlignment(Pos.CENTER);

        ImageView goldIcon = ImageLoader.loadImage(GOLD_ICON_URL, ICON_SIZE, ICON_SIZE);
        money = new DefaultLabel(
                Client.getInstance().getAccount().getMoney() + "$",
                UIConstants.DEFAULT_FONT, Color.WHITE
        );
        searchField = new SearchField();

        AtomicReference<String> oldValue = new AtomicReference<>("");
        searchField.setOnKeyPressed(event -> {
            if (searchField.getText().equals(oldValue.get())) return;

            oldValue.set(searchField.getText());
            search();
        });

        getChildren().addAll(goldIcon, money, searchField);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("money")) {
            Platform.runLater(() -> money.setText(evt.getNewValue() + "$"));
        }
    }

    public void clear() {
        searchField.clear();
        search();
    }

    public void search() {
        ShopController.getInstance().searchInShop(searchField.getText());
    }
}