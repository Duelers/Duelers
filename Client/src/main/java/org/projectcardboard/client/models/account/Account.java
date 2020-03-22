package org.projectcardboard.client.models.account;

import org.projectcardboard.client.models.card.Deck;
import org.projectcardboard.client.models.card.TempDeck;
import shared.models.account.AccountType;
import shared.models.account.BaseAccount;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Account extends BaseAccount<Deck, Collection, MatchHistory> {
    private transient final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public Account(TempAccount account) {
        super(account.getUsername(), account.getPassword(), account.getCollection(), account.getAccountType());
        if (account.getDecks() != null) {
            for (TempDeck deck : account.getDecks()) {
                this.decks.add(new Deck(deck, collection));
            }
        }
        this.matchHistories = account.getMatchHistories();
        this.mainDeck = getDeck(account.getMainDeckName());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass().getName().equals(AccountInfo.class.getName())) {
            AccountInfo accountInfo = (AccountInfo) obj;
            if (this.username.equals(accountInfo.getUsername())) return true;
        }
        if (!obj.getClass().getName().equals(Account.class.getName())) return false;
        Account account = (Account) obj;
        return this.username.equals(account.username);
    }


    public Deck getDeck(String deckName) {
        for (Deck deck : decks) {
            if (deck.hasName(deckName)) {
                return deck;
            }
        }
        return null;
    }

    public boolean isMainDeck(Deck deck) {
        return deck.equals(this.mainDeck);
    }

    public void update(TempAccount account) { //Todo this stops all fields from being final. It should make a new account instead.
        if (!username.equals(account.getUsername())) {
            String old = username;
            username = account.getUsername();
            support.firePropertyChange("username", old, username);
        }
        if (!password.equals(account.getPassword())) {
            password = account.getPassword();
        }
        if (!collection.equals(account.getCollection())) {
            Collection old = collection;
            collection = account.getCollection();
            support.firePropertyChange("collection", old, collection);
        }

        if (!decksEqual(account.getDecks())) {
            ArrayList<Deck> newDecks = new ArrayList<>();

            for (TempDeck deck : account.getDecks()) {
                newDecks.add(new Deck(deck, collection));
            }
            List<Deck> old = decks;
            decks = newDecks;
            support.firePropertyChange("decks", old, decks);
            if (!mainDecksEqual(account)) {
                Deck oldMain = mainDeck;
                mainDeck = getDeck(account.getMainDeckName());
                support.firePropertyChange("main_deck", oldMain, mainDeck);
            }
        } else if (!mainDecksEqual(account)) {
            Deck old = mainDeck;
            mainDeck = getDeck(account.getMainDeckName());
            support.firePropertyChange("main_deck", old, mainDeck);
        }
        matchHistories = account.getMatchHistories();
        accountType = account.getAccountType();
    }

    private boolean mainDecksEqual(TempAccount account) {
        return (
                (mainDeck == null && account.getMainDeckName() == null) ||
                        (mainDeck != null && account.getMainDeckName() != null && mainDeck.getName().equals(account.getMainDeckName()))
        );
    }

    private boolean decksEqual(List<TempDeck> decks) {
        if (this.decks.size() != decks.size()) return false;

        for (TempDeck deck : decks) {
            if (!this.decks.contains(deck)) return false;
        }
        return true;
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }

    public AccountType getAccountType() {
        return accountType;
    }
}