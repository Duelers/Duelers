package models.gui;

import controller.MainMenuController;
import javafx.scene.control.Button;
import models.card.Card;
import models.card.CardType;

public class CardRequestView {
    private String cardName;
    private CardType cardType;
    private Button accept = new Button("Accept");
    private Button reject = new Button("Reject");

    CardRequestView(Card card) {
        cardName = card.getName();
        cardType = card.getType();
        accept.setOnMouseClicked(event -> MainMenuController.getInstance().acceptCustomCard(card.getName()));
        reject.setOnMouseClicked(event -> MainMenuController.getInstance().rejectCustomCard(card.getName()));
    }

    public String getCardName() {
        return cardName;
    }

    public CardType getCardType() {
        return cardType;
    }

    public Button getAccept() {
        return accept;
    }

    public Button getReject() {
        return reject;
    }
}
