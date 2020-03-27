package shared.models.account;

import shared.models.card.BaseTempDeck;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BaseTempAccount<
        CollectionType extends BaseCollection,
        TempDeckType extends BaseTempDeck,
        MatchHistoryType extends BaseMatchHistory> {
    private final String username;
    private final String password;
    private final AccountType accountType;
    private final CollectionType collection;
    protected final List<TempDeckType> decks = new ArrayList<>();
    protected String mainDeckName;
    protected final List<MatchHistoryType> matchHistories;

    public BaseTempAccount(String username,
                           String password,
                           AccountType accountType,
                           CollectionType collection) {
        this(username, password, accountType, collection, new ArrayList<>());
    }

    public BaseTempAccount(String username,
                           String password,
                           AccountType accountType,
                           CollectionType collection,
                           List<MatchHistoryType> matchHistories) {
        this(username, password, accountType, collection, matchHistories, new ArrayList<>(), null);
    }

    public BaseTempAccount(String username, String password,
                           AccountType accountType,
                           CollectionType collection,
                           List<MatchHistoryType> matchHistories,
                           List<TempDeckType> decks,
                           String mainDeckName) {
        this.username = username;
        this.password = password;
        this.accountType = accountType;
        this.collection = collection;
        this.matchHistories = matchHistories;

        this.decks.addAll(decks);
        this.mainDeckName = mainDeckName;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public CollectionType getCollection() {
        return collection;
    }

    public List<TempDeckType> getDecks() {
        return Collections.unmodifiableList(decks);
    }

    public String getMainDeckName() {
        return mainDeckName;
    }

    public List<MatchHistoryType> getMatchHistories() {
        return matchHistories;
    }

    public AccountType getAccountType() {
        return accountType;
    }
}
