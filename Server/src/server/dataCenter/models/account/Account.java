package server.dataCenter.models.account;

import server.dataCenter.DataCenter;
import server.dataCenter.models.card.Deck;
import server.dataCenter.models.card.ServerCard;
import server.dataCenter.models.card.TempDeck;
import server.exceptions.ClientException;
import server.exceptions.LogicException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static server.dataCenter.models.account.AccountType.NORMAL;

public class Account {
    private final String username;
    private final String password;
    private AccountType accountType;
    private final Collection collection;
    private final List<Deck> decks = new ArrayList<>();
    private Deck mainDeck;
    private List<MatchHistory> matchHistories = new ArrayList<>();

    public Account(String username, String password) {
        this.username = username;
        this.password = password;
        this.collection = new Collection();
        this.accountType = NORMAL;
    }

    public Account(TempAccount account) {
        this.username = account.getUsername();
        this.password = account.getPassword();
        this.collection = account.getCollection();
        if (account.getDecks() != null) {
            for (TempDeck deck : account.getDecks()) {
                this.decks.add(new Deck(deck, collection));
            }
        }
        if (account.getMainDeckName() != null)
            this.mainDeck = getDeck(account.getMainDeckName());
        this.matchHistories = account.getMatchHistories();
        this.accountType = account.getAccountType();
    }

    private boolean hasDeck(String deckName) {
        if (deckName == null)
            return false;
        for (Deck deck : decks) {
            if (deck.getName().equalsIgnoreCase(deckName))
                return true;
        }
        return false;
    }

    public Deck getDeck(String deckName) {
        if (deckName == null)
            return null;
        for (Deck deck : decks) {
            if (deck.getName().equalsIgnoreCase(deckName)) {
                return deck;
            }
        }
        return null;
    }

    public void addDeck(String deckName) throws LogicException {
        if (hasDeck(deckName)) {
            throw new ClientException("new deck's name was duplicate.");
        }
        decks.add(new Deck(deckName));
    }

    public void addDeck(Deck deck) throws ClientException {
        if (hasDeck(deck.getName())) {
            throw new ClientException("new deck's name was duplicate.");
        }
        decks.add(deck);
    }

    public void deleteDeck(String deckName) throws LogicException {
        if (!hasDeck(deckName)) {
            throw new ClientException("deck was not found.");
        }
        decks.remove(getDeck(deckName));
    }

    public void buyCard(String cardName, Collection originalCards) throws LogicException {
        ServerCard card = DataCenter.getCard(cardName, originalCards);
        if (card == null) {
            throw new ClientException("invalid card name");
        }
        collection.addCard(cardName, originalCards, username);
    }

    public void addCardToDeck(String cardId, String deckName) throws LogicException {
        if (!hasDeck(deckName)) {
            throw new ClientException("deck was not found.");
        } else if (!collection.hasCard(cardId)) {
            throw new ClientException("invalid card id.");
        } else {
            getDeck(deckName).addCard(cardId, collection);
        }
    }

    public void removeCardFromDeck(String cardId, String deckName) throws LogicException {
        if (!hasDeck(deckName)) {
            throw new ClientException("deck was not found.");
        } else {
            getDeck(deckName).removeCard(collection.getCard(cardId));
        }
    }

    public void selectDeck(String deckName) throws LogicException {
        if (!hasDeck(deckName)) {
            throw new ClientException("deck was not found.");
        } else {
            mainDeck = getDeck(deckName);
        }
    }

    public void addMatchHistory(MatchHistory matchHistory) {
        matchHistories.add(matchHistory);
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Collection getCollection() {
        return collection;
    }

    public void updateCollection(Collection collection){
        this.getCollection().clearCollection();
        for(ServerCard card : collection.getHeroes()){
            for(int i = 0; i < 1; i++){   
                this.collection.addCard(card.getName(), collection, this.username);
            }
        }
        for(ServerCard card : collection.getMinions()){
            for(int i = 0; i < 3; i++){   
                this.collection.addCard(card.getName(), collection, this.username);
            }
        }
        for(ServerCard card : collection.getSpells()){
            for(int i = 0; i < 3; i++){   
                this.collection.addCard(card.getName(), collection, this.username);
            }
        }
    }

    public Deck getMainDeck() {
        return mainDeck;
    }

    public boolean hasValidMainDeck() {
        return mainDeck != null && mainDeck.isValid();
    }

    List<MatchHistory> getMatchHistories() {
        return Collections.unmodifiableList(matchHistories);
    }

    public int getWins() {
        return (int) matchHistories.stream().filter(MatchHistory::isAmIWinner).count();
    }

    List<Deck> getDecks() {
        return Collections.unmodifiableList(decks);
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }
}