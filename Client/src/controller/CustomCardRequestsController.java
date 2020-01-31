package controller;

import models.account.Collection;
import models.card.Card;
import models.message.DataName;
import models.message.Message;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import static models.Constants.SERVER_NAME;

public class CustomCardRequestsController {
    private static CustomCardRequestsController controller;
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    private Collection cards;

    private CustomCardRequestsController() {
    }

    public static CustomCardRequestsController getInstance() {
        if (controller == null) {
            controller = new CustomCardRequestsController();
        }
        return controller;
    }


    void setCustomCardRequests(Collection cards) {
        this.cards = cards;
        support.firePropertyChange("set_cards", null, cards);
    }

    void addCard(Card card) {
        if (cards != null) {
            cards.addCard(card);
            support.firePropertyChange("add_card", null, card);
        }
    }

    void removeCard(String cardName) {
        if (cards != null) {
            Card card;
            if (null != (card = cards.removeCard(cardName))) {
                support.firePropertyChange("remove_card", null, card);
            }
        }
    }

    public void addListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    public void removeListener(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }

    public void requestCustomCardRequests() {
        Client.getInstance().addToSendingMessagesAndSend(
                Message.makeGetDataMessage(SERVER_NAME, DataName.CUSTOM_CARDS)
        );
    }
}
