package controller;

import models.Constants;
import models.account.Collection;
import models.card.Card;
import models.message.DataName;
import models.message.Message;
import view.ShopMenu;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class ShopController {
    private static ShopController ourInstance;
    private Collection originalCards;
    private Collection showingCards;
    private PropertyChangeSupport support = new PropertyChangeSupport(this);

    private ShopController() {
    }

    public static ShopController getInstance() {
        if (ourInstance == null) {
            ourInstance = new ShopController();
            Client.getInstance().addToSendingMessagesAndSend(
                    Message.makeGetDataMessage(Constants.SERVER_NAME, DataName.ORIGINAL_CARDS));
        }
        return ourInstance;
    }

    static boolean isLoaded() {
        return ourInstance != null;
    }

    public void buy(String cardName) {
        Client.getInstance().addToSendingMessagesAndSend(Message.makeBuyCardMessage(Constants.SERVER_NAME, cardName));
    }

    public void sell(String cardName) {
        Card card = Client.getInstance().getAccount().getCollection().findLast(cardName);

        if (card == null) {
            Client.getInstance().getCurrentShow().showError("You don't have such card");
            return;
        }
        Client.getInstance().addToSendingMessagesAndSend(
                Message.makeSellCardMessage(Constants.SERVER_NAME, card.getCardId()));
    }

    public Collection getShowingCards() {
        return showingCards;
    }

    public void searchInShop(String cardName) {
        Collection result = originalCards.searchCollection(cardName);
        support.firePropertyChange("search_result", showingCards, result);
        showingCards = result;
    }

    void addCard(Card customCard) {
        originalCards.addCard(customCard);
        if (Client.getInstance().getCurrentShow() instanceof ShopMenu) {
            ((ShopMenu) Client.getInstance().getCurrentShow()).search();
        }
        ShopAdminController.getInstance().addCard(customCard);
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }

    public Collection getOriginalCards() {
        return originalCards;
    }

    synchronized void setOriginalCards(Collection originalCards) {
        Collection old = this.originalCards;
        this.originalCards = originalCards;
        this.showingCards = originalCards.searchCollection("");
        this.notify();
        ShopAdminController.getInstance().setOriginalCards(old, originalCards);
    }
}
