package models.gui;

import controller.ShopAdminController;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import models.card.Card;
import models.card.CardType;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import static javafx.scene.input.KeyCode.ENTER;

public class CardAdminView implements PropertyChangeListener {
    private String cardName;
    private CardType cardType;
    private ObjectProperty<Integer> remainingNumber;
    private NumberField numberField;

    CardAdminView(Card card) {
        cardName = card.getName();
        cardType = card.getType();
        remainingNumber = new SimpleObjectProperty<>(card.getRemainingNumber());
        numberField = new NumberField("");

        card.addListener(this);
        numberField.setOnKeyPressed(event -> {
            if (event.getCode() == ENTER) {
                ShopAdminController.getInstance().changeValueRequest(card, numberField.getValue());
                numberField.clear();
            }
        });
    }

    public String getCardName() {
        return cardName;
    }

    public CardType getCardType() {
        return cardType;
    }

    ObjectProperty<Integer> remainingNumberProperty() {
        return remainingNumber;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("new_value")) {
            Platform.runLater(() -> remainingNumber.setValue((Integer) evt.getNewValue()));
        }
    }

    public NumberField getNumberField() {
        return numberField;
    }
}
