package shared.models.account;

import shared.models.card.BaseDeck;

import java.util.ArrayList;
import java.util.List;

import static shared.models.account.AccountType.NORMAL;

public class BaseAccount <
        DeckType extends BaseDeck,
        CollectionType extends BaseCollection,
        MatchHistoryType extends BaseMatchHistory>{
    protected String username;
    protected String password;
    protected AccountType accountType;
    protected CollectionType collection;
    protected List<DeckType> decks = new ArrayList<>();
    protected DeckType mainDeck;
    protected List<MatchHistoryType> matchHistories = new ArrayList<>();


    public BaseAccount(String username, String password, CollectionType collection) {
        this(username, password, collection, NORMAL);
    }

    public BaseAccount(String username, String password, CollectionType collection, AccountType accountType) {
        this.username = username;
        this.password = password;
        this.collection = collection;
        this.accountType = accountType;
    }

}
