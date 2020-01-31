package models.gui;


import controller.ShopAdminController;
import controller.ShopController;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.account.Collection;
import models.card.Card;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.stream.Collectors;

import static models.gui.UIConstants.SCALE;

public class ShopAdminList extends TableView implements PropertyChangeListener {

    private static final double WIDTH = 1500 * SCALE;
    private static final double HEIGHT = 1800 * SCALE;

    public ShopAdminList() {
        super();

        setMaxSize(WIDTH, HEIGHT);

        ShopAdminController.getInstance().addListener(this);
        if (ShopController.getInstance().getOriginalCards() != null) {
            setOriginalCards(ShopController.getInstance().getOriginalCards());
        }

        TableColumn<CardAdminView, Integer> index = new TableColumn<>("Index");
        index.setSortable(false);
        index.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(getItems().indexOf(param.getValue()) + 1)
        );
        index.setMaxWidth(100 * SCALE);

        TableColumn<CardAdminView, String> cardName = new TableColumn<>("Name");
        cardName.setCellValueFactory(new PropertyValueFactory<>("cardName"));
        cardName.setMinWidth(300 * SCALE);

        TableColumn<CardAdminView, String> cardType = new TableColumn<>("Card type");
        cardType.setCellValueFactory(new PropertyValueFactory<>("cardType"));
        cardType.setMinWidth(300 * SCALE);

        TableColumn<CardAdminView, Integer> remainingNumber = new TableColumn<>("Remaining number");
        remainingNumber.setCellValueFactory(param -> param.getValue().remainingNumberProperty());
        remainingNumber.setMinWidth(300 * SCALE);

        TableColumn<CardAdminView, NumberField> textField = new TableColumn<>("Change remaining");
        textField.setCellValueFactory(new PropertyValueFactory<>("numberField"));
        textField.setMinWidth(400 * SCALE);

        getColumns().addAll(index, cardName, cardType, remainingNumber, textField);
    }

    private void setOriginalCards(Collection originalCards) {
        getItems().addAll(originalCards.getHeroes().stream().map(CardAdminView::new).collect(Collectors.toList()));
        getItems().addAll(originalCards.getMinions().stream().map(CardAdminView::new).collect(Collectors.toList()));
        getItems().addAll(originalCards.getSpells().stream().map(CardAdminView::new).collect(Collectors.toList()));
        getItems().addAll(originalCards.getItems().stream().map(CardAdminView::new).collect(Collectors.toList()));
    }

    private void addCard(Card card) {
        getItems().add(new CardAdminView(card));
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case "original_cards":
                Platform.runLater(() -> setOriginalCards((Collection) evt.getNewValue()));
                break;
            case "add_card":
                Platform.runLater(() -> addCard((Card) evt.getNewValue()));
                break;
        }
    }
}
