package server.dataCenter.models.card;

import server.GameServer;
import server.dataCenter.models.account.Collection;
import server.exceptions.ClientException;
import server.exceptions.LogicException;
import shared.models.card.BaseDeck;

import java.util.ArrayList;

public class Deck extends BaseDeck<ServerCard> {
  public Deck(String deckName, ServerCard hero, ArrayList<ServerCard> cards) {
    super(deckName, hero, cards);
  }

  public Deck(Deck deck) {
    super(deck.deckName);
    if (deck.hero != null) {
      this.hero = new ServerCard(deck.hero);
    }
    for (ServerCard card : deck.cards) {
      this.cards.add(new ServerCard(card));
    }
  }

  public Deck(TempDeck tempDeck, Collection collection) {
    super(tempDeck.getDeckName());
    if (collection == null)
      return;
    this.hero = collection.getCard(tempDeck.getHeroId());
    for (String cardId : tempDeck.getCardIds()) {
      this.cards.add(collection.getCard(cardId));
    }
  }

  public Deck(String deckName) {
    super(deckName);
  }

  public boolean hasCardOrHeroWithId(String cardId) {
    if (hero != null && hero.getCardId().equalsIgnoreCase(cardId)) {
      return true;
    }
    for (ServerCard card : cards) {
      if (card.getCardId().equalsIgnoreCase(cardId))
        return true;
    }
    return false;
  }

  public void addCard(String cardId, Collection collection) throws LogicException {
    if (hasCardOrHeroWithId(cardId)) {
      throw new ClientException("deck had this card.");
    }
    addCard(collection.getCard(cardId));
  }


  public void addCard(ServerCard card) throws LogicException {
    if (card == null)
      throw new ClientException("this card isn't in your collection!");
    switch (card.getType()) {
      case HERO:
        if (hero != null)
          throw new ClientException("you can't have more than 1 hero!");
        hero = card;
        break;
      case MINION:
      case SPELL:
        cards.add(card);
        break;
      default:
        GameServer.serverPrint("Error, card does not have valid type.");
        break;
    }
  }

  public void removeCard(ServerCard card) throws ClientException {
    if (!hasCardOrHeroWithId(card.getCardId())) {
      throw new ClientException("deck doesn't have this card.");
    }
    if (card.equals(hero)) {
      hero = null;
    }
    cards.remove(card);
  }


  public void makeCustomGameDeck() {
    String customGamePrefix = "customGame_";
    hero.setCardId(customGamePrefix + hero.getCardId());
    deckName = customGamePrefix + deckName;
    for (ServerCard card : cards) {
      card.setCardId(customGamePrefix + card.getCardId());
    }
  }
}
