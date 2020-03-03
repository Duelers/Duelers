package server.dataCenter.models.account;

import server.dataCenter.models.card.Deck;
import server.dataCenter.models.card.TempDeck;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TempAccount {
    private String username;
    private String password;
    private AccountType accountType;
    private Collection collection;
    private List<TempDeck> decks = new ArrayList<>();
    private String mainDeckName;
    private List<MatchHistory> matchHistories;

    public TempAccount(Account account) {
        this.username = account.getUsername();
        this.password = account.getPassword();
        this.collection = account.getCollection();
        for (Deck deck : account.getDecks()) {
            this.decks.add(new TempDeck(deck));
        }
        if (account.getMainDeck() != null) {
            this.mainDeckName = account.getMainDeck().getDeckName();
        }
        this.matchHistories = account.getMatchHistories();
        this.accountType = account.getAccountType();
    }

    public String getUsername() {
        return username;
    }

    String getPassword() {
        return password;
    }

    public Collection getCollection() {
        return collection;
    }

    List<TempDeck> getDecks() {
        return Collections.unmodifiableList(decks);
    }

    String getMainDeckName() {
        return mainDeckName;
    }

    List<MatchHistory> getMatchHistories() {
        return matchHistories;
    }

    public AccountType getAccountType() {
        return accountType;
    }
}
