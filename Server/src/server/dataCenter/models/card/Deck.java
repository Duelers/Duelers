package server.dataCenter.models.card;

import server.GameServer;
import server.dataCenter.models.account.Collection;
import server.exceptions.ClientException;
import server.exceptions.LogicException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private String deckName;
    private ServerCard hero;
    private ServerCard item;
    private List<ServerCard> others = new ArrayList<>();

    public Deck(String deckName, ServerCard hero, ServerCard item, ArrayList<ServerCard> others) {
        this.deckName = deckName;
        this.hero = hero;
        this.item = item;
        this.others = others;
    }

    public Deck(Deck deck) {
        this.deckName = deck.deckName;
        if (deck.hero != null) {
            this.hero = new ServerCard(deck.hero);
        }
        if (deck.item != null) {
            this.item = new ServerCard(deck.item);
        }
        for (ServerCard card : deck.others) {
            others.add(new ServerCard(card));
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
        for (ServerCard card : others) {
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
                others.add(card);
                break;
            default:
                GameServer.serverPrint("Error, card does not have valid type.");
                break;
        }
    }

    public void removeCard(ServerCard card) throws ClientException {
        if (!hasCard(card.getCardId())) {
            throw new ClientException("deck doesn't have this card.");
        }
        if (hero.equals(card))
            hero = null;
        if (item.equals(card))
            item = null;
        others.remove(card);
    }

    public boolean isValid() {
        if (hero == null) return false;
        return others.size() == 20;
    }

    public void copyCards() {//TODO:reCode
        if (hero != null) {
            this.hero = new ServerCard(hero);
            this.hero.setCardId(makeId(hero, 1));
        }

        List<ServerCard> oldOthers = this.others;
        this.others = new ArrayList<>();
        for (ServerCard other : oldOthers) {
            ServerCard card = new ServerCard(other);
            card.setCardId(makeId(card, numberOf(card.getName()) + 1));
            others.add(card);
        }
    }

    private String makeId(ServerCard card, int number) {
        return deckName.replaceAll(" ", "") + "_" +
                card.getName().replaceAll(" ", "") + "_" +
                number;
    }

    private int numberOf(String name) {
        if (hero.getName().equalsIgnoreCase(name) || item.getName().equalsIgnoreCase(name)) return 0;
        int number = 0;
        for (ServerCard card : others) {
            if (card.getName().equalsIgnoreCase(name)) number++;
        }
        return number;
    }

    public String getDeckName() {
        return deckName;
    }

    public ServerCard getHero() {
        return hero;
    }

    public List<ServerCard> getOthers() {
        return Collections.unmodifiableList(others);
    }

    public ServerCard getItem() {
        return item;
    }

    public void makeCustomGameDeck() {
        hero.setCardId("customGame_" + hero.getCardId());
        if (item != null)
            item.setCardId("customGame_" + item.getCardId());
        deckName = "customGame_" + deckName;
        for (ServerCard card : others) {
            card.setCardId("customGame_" + card.getCardId());
        }
    }

    public String getName() {
        return deckName;
    }
}