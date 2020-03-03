package server.dataCenter.models.account;

import server.dataCenter.DataCenter;
import server.dataCenter.models.card.Deck;
import server.dataCenter.models.card.TempDeck;
import server.exceptions.ClientException;
import server.exceptions.LogicException;
import shared.models.card.Card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static server.dataCenter.models.account.AccountType.NORMAL;

public class Account {
    private String username;
    private String password;
    private AccountType accountType;
    private Collection collection;
    private List<Deck> decks = new ArrayList<>();
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
            if (deck.getDeckName().equalsIgnoreCase(deckName))
                return true;
        }
        return false;
    }

    public Deck getDeck(String deckName) {
        if (deckName == null)
            return null;
        for (Deck deck : decks) {
            if (deck.getDeckName().equalsIgnoreCase(deckName)) {
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
        if (hasDeck(deck.getDeckName())) {
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
        Card card = DataCenter.getCard(cardName, originalCards);
        if (card == null) {
            throw new ClientException("invalid card name");
        }
        //removed so we can buy as many cards as we want
        //if (card.getRemainingNumber() <= 0) {
            //throw new ClientException("Shop doesn't have any of this card.");
        //}
        collection.addCard(cardName, originalCards, username);
        //removed, no longer needed
        //DataCenter.getInstance().changeCardNumber(cardName, -1);
    }

    public void sellCard(String cardId) throws LogicException {
        Card card = collection.getCard(cardId);
        if (card == null) {
            throw new ClientException("invalid card id");
        }

        collection.removeCard(card);
        for (Deck deck : decks) {
            if (deck.hasCard(cardId)) {
                deck.removeCard(card);
            }
        }
        DataCenter.getInstance().changeCardNumber(card.getName(), +1);
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
        return matchHistories.stream().filter(MatchHistory::isAmIWinner).collect(Collectors.toList()).size();
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