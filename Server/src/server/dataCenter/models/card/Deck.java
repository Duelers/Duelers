package server.dataCenter.models.card;

import server.GameServer;
import server.dataCenter.models.account.Collection;
import server.exceptions.ClientException;
import server.exceptions.LogicException;
import shared.models.card.Card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private String deckName;
    private Card hero;
    private Card item;
    private List<Card> others = new ArrayList<>();

    public Deck(String deckName, Card hero, Card item, ArrayList<Card> others) {
        this.deckName = deckName;
        this.hero = hero;
        this.item = item;
        this.others = others;
    }

    public Deck(Deck deck) {
        this.deckName = deck.deckName;
        if (deck.hero != null) {
            this.hero = new Card(deck.hero);
        }
        if (deck.item != null) {
            this.item = new Card(deck.item);
        }
        for (Card card : deck.others) {
            others.add(new Card(card));
        }
    }

    public Deck(TempDeck tempDeck, Collection collection) {
        this.deckName = tempDeck.getDeckName();
        if (collection == null)
            return;
        this.hero = collection.getCard(tempDeck.getHeroId());
        this.item = collection.getCard(tempDeck.getItemId());
        for (String cardId : tempDeck.getOthersIds()) {
            others.add(collection.getCard(cardId));
        }
    }

    public Deck(String deckName) {
        this.deckName = deckName;
    }

    public boolean hasCard(String cardId) {
        if (hero != null && hero.getCardId().equalsIgnoreCase(cardId))
            return true;
        if (item != null && item.getCardId().equalsIgnoreCase(cardId))
            return true;
        for (Card card : others) {
            if (card.getCardId().equalsIgnoreCase(cardId))
                return true;
        }
        return false;
    }

    public void addCard(String cardId, Collection collection) throws LogicException {
        if (hasCard(cardId)) {
            throw new ClientException("deck had this card.");
        }
        addCard(collection.getCard(cardId));
    }


    public void addCard(Card card) throws LogicException {
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
                others.add(card);
                break;
            default:
                GameServer.getInstance().serverPrint("Error!");
                break;
        }
    }

    public void removeCard(Card card) throws ClientException {
        if (!hasCard(card.getCardId())) {
            throw new ClientException("deck doesn't have this card.");
        }
        if (hero == card)
            hero = null;
        if (item == card)
            item = null;
        others.remove(card);
    }

    public boolean isValid() {
        if (hero == null) return false;
        return others.size() == 20;
    }

    public void copyCards() {//TODO:reCode
        if (hero != null) {
            this.hero = new Card(hero);
            this.hero.setCardId(makeId(hero, 1));
        }

        List<Card> oldOthers = this.others;
        this.others = new ArrayList<>();
        for (Card other : oldOthers) {
            Card card = new Card(other);
            card.setCardId(makeId(card, numberOf(card.getName()) + 1));
            others.add(card);
        }
    }

    private String makeId(Card card, int number) {
        return deckName.replaceAll(" ", "") + "_" +
                card.getName().replaceAll(" ", "") + "_" +
                number;
    }

    private int numberOf(String name) {
        if (hero.getName().equalsIgnoreCase(name) || item.getName().equalsIgnoreCase(name)) return 0;
        int number = 0;
        for (Card card : others) {
            if (card.getName().equalsIgnoreCase(name)) number++;
        }
        return number;
    }

    public String getDeckName() {
        return deckName;
    }

    public Card getHero() {
        return hero;
    }

    public List<Card> getOthers() {
        return Collections.unmodifiableList(others);
    }

    public Card getItem() {
        return item;
    }

    public void makeCustomGameDeck() {
        hero.setCardId("customGame_" + hero.getCardId());
        if (item != null)
            item.setCardId("customGame_" + item.getCardId());
        deckName = "customGame_" + deckName;
        for (Card card : others) {
            card.setCardId("customGame_" + card.getCardId());
        }
    }

    public String getName() {
        return deckName;
    }
}