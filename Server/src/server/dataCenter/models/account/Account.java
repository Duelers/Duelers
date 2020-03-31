package server.dataCenter.models.account;

import org.glassfish.tyrus.server.Server;
import server.dataCenter.DataCenter;
import server.dataCenter.models.card.Deck;
import server.dataCenter.models.card.ServerCard;
import server.dataCenter.models.card.TempDeck;
import server.exceptions.ClientException;
import server.exceptions.LogicException;
import shared.models.account.AccountType;
import shared.models.account.BaseAccount;

public class Account extends BaseAccount<Deck, Collection, MatchHistory> {
  public Account(String username, String password) {
    super(username, password, new Collection());
  }

  public Account(TempAccount account) {
    super(account.getUsername(), account.getPassword(), account.getCollection(),
        account.getAccountType());

    if (account.getDecks() != null) {
      for (TempDeck deck : account.getDecks()) {
        this.decks.add(new Deck(deck, collection));
      }
    }
    if (account.getMainDeckName() != null)
      this.mainDeck = getDeck(account.getMainDeckName());
    this.matchHistories = account.getMatchHistories();
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

  public String getPassword() {
    return password;
  }

  public void updateCollection(Collection collection) {
    this.getCollection().clearCollection();

    System.out.println("Adding " + collection.getHeroes().size() + " Generals to account");
    for (ServerCard card : collection.getHeroes()) {
      this.collection.addCard(card.getName(), collection, this.username);
    }

    System.out.println("Adding " + collection.getMinions().size() + " minions to account");
    for (ServerCard card : collection.getMinions()) {
      for (int i = 0; i < 3; i++) {
        this.collection.addCard(card.getName(), collection, this.username);
      }
    }

    System.out.println("Adding " + collection.getSpells().size() + " spells to account");
    for (ServerCard card : collection.getSpells()) {
      for (int i = 0; i < 3; i++) {
        this.collection.addCard(card.getName(), collection, this.username);
      }
    }
  }

  public void addMatchHistory(MatchHistory matchHistory) {
    matchHistories.add(matchHistory);
  }

  public Deck getMainDeck() {
    return mainDeck;
  }

  public boolean hasValidMainDeck() {
    return mainDeck != null && mainDeck.isValid();
  }

  public int getWins() {
    return (int) matchHistories.stream().filter(MatchHistory::getAmIWinner).count();
  }

  public void setAccountType(AccountType accountType) {
    this.accountType = accountType;
  }
}
