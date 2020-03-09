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
    private List<ServerCard> others = new ArrayList<>();

    public Deck(String deckName, ServerCard hero, ArrayList<ServerCard> others) {
        this.deckName = deckName;
        this.hero = hero;
        this.others = others;
    }

    public Deck(Deck deck) {
        this.deckName = deck.deckName;
        if (deck.hero != null) {
            this.hero = new ServerCard(deck.hero);
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
        for (String cardId : tempDeck.getOthersIds()) {
            others.add(collection.getCard(cardId));
        }
    }

    public Deck(String deckName) {
        this.deckName = deckName;
    }

    public boolean hasCard(String cardId) {
        if (hero != null && hero.getCardId().equalsIgnoreCase(cardId)) {
            return true;
        }
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
        if (card.equals(hero)) {
            hero = null;
        }
        others.remove(card);
    }

    public boolean isValid() {
        if (hero == null) return false;
        return others.size() == 20;
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

    public void makeCustomGameDeck() {
        String customGamePrefix = "customGame_";
        hero.setCardId(customGamePrefix + hero.getCardId());
        deckName = customGamePrefix + deckName;
        for (ServerCard card : others) {
            card.setCardId(customGamePrefix + card.getCardId());
        }
    }

    public String getName() {
        return deckName;
    }
}