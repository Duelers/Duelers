package controller;

import Config.Config;
import models.account.Collection;
import shared.models.card.Card;
import models.card.Deck;
import models.card.ExportedDeck;
import models.message.Message;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class CollectionMenuController implements PropertyChangeListener {
    private static CollectionMenuController ourInstance;
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    private Collection allShowingCards;
    private Collection currentShowingCards;
    private static final String SERVER_NAME = Config.getInstance().getProperty("SERVER_NAME");

    private CollectionMenuController() {
        Client.getInstance().getAccount().addPropertyChangeListener(this);
        allShowingCards = Client.getInstance().getAccount().getCollection().toShowing();
        currentShowingCards = allShowingCards;
    }

    public static CollectionMenuController getInstance() {
        if (ourInstance == null) {
            ourInstance = new CollectionMenuController();
        }
        return ourInstance;
    }

    public void newDeck(String deckName) {
        Client.getInstance().addToSendingMessagesAndSend(
                Message.makeCreateDeckMessage(SERVER_NAME, deckName));
    }

    public void addCardToDeck(Deck deck, String cardName) {
        String cardID = Client.getInstance().getAccount().getCollection().canAddCardTo(cardName, deck);
        if (cardID == null) {
            Client.getInstance().getCurrentShow().showError("Can not add this card");
            return;
        }
        Client.getInstance().addToSendingMessagesAndSend(
                Message.makeAddCardToDeckMessage(SERVER_NAME, deck.getName(), cardID));
    }

    public void removeCardFromDeck(Deck deck, String cardName) {
        Card card = deck.getCard(cardName);
        if (card == null) {
            Client.getInstance().getCurrentShow().showError("This card is not in this deck");
            return;
        }
        String cardID = card.getCardId();
        Client.getInstance().addToSendingMessagesAndSend(
                Message.makeRemoveCardFromDeckMessage(SERVER_NAME, deck.getName(), cardID));
    }

    public void removeDeck(String deckName) {
        Client.getInstance().addToSendingMessagesAndSend(
                Message.makeRemoveDeckMessage(SERVER_NAME, deckName));
    }

    public void selectDeck(String deckName) {
        Client.getInstance().addToSendingMessagesAndSend(
                Message.makeSelectDeckMessage(SERVER_NAME, deckName));
    }

    public void search(String cardName) {
        Collection result = allShowingCards.searchCollection(cardName);
        support.firePropertyChange("search_result", currentShowingCards, result);
        currentShowingCards = result;
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }

    public Collection getCurrentShowingCards() {
        return currentShowingCards;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("collection")) {
            this.allShowingCards = ((Collection) evt.getNewValue()).toShowing();
            this.currentShowingCards = allShowingCards;
        }
    }

    public void importDeck(ExportedDeck exportedDeck) {
        Client.getInstance().addToSendingMessagesAndSend(
                Message.makeImportDeckMessage(SERVER_NAME, exportedDeck));
    }
}
