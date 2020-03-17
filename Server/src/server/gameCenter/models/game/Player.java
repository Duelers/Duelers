package server.gameCenter.models.game;

import server.dataCenter.models.account.MatchHistory;
import server.dataCenter.models.card.ServerCard;
import shared.Constants;
import shared.models.card.CardType;
import server.dataCenter.models.card.Deck;
import server.exceptions.ClientException;
import server.exceptions.LogicException;
import shared.models.game.BasePlayer;
import shared.models.game.map.Cell;
//import server.dataCenter.models.Constants;

import java.util.*;

public class Player extends BasePlayer<ServerCard, ServerTroop> {
    private final Deck deck;
    private MatchHistory matchHistory;
    private int numTimesReplacedThisTurn;
    private int maxNumReplacePerTurn;

    Player(Deck mainDeck, String userName, int playerNumber) {
        super(userName, 0, new ArrayList<>(), new ArrayList<>(), null,
                playerNumber, new ArrayList<>(), null);
        //this.playerNumber = playerNumber;
        //this.userName = userName;
        this.deck = new Deck(mainDeck);
        //setNextCard();
        //for (int i = 0; i < 3; i++) {
            //addNextCardToHand();
        //}
        ServerCard[] drawnCards = getCardsFromDeck(3);
        addCardsToHand(drawnCards);
        this.numTimesReplacedThisTurn = 0;
        this.maxNumReplacePerTurn = 1;
    }

    public void tryInsert(ServerCard card) throws ClientException {
        if (card == null)
            throw new ClientException("card id is not valid");

        if (card.getManaCost() > currentMP)
            throw new ClientException("not enough mana");
    }

    public void insert(ServerCard card) {
        hand.remove(card);
        currentMP -= card.getManaCost();
    }

    public ServerCard getCardFromHand(String cardId) {
        for (ServerCard card : hand) {
            if (card.getCardId().equalsIgnoreCase(cardId)) {
                return card;
            }
        }
        return null;
    }

    public ServerCard removeCardFromHand(String cardID) throws ClientException {
        ServerCard cardToRemove = null;

        for (int i = 0; i < hand.size(); i++) {
            ServerCard tempCard = hand.get(i);
            if (tempCard.getCardId().equalsIgnoreCase(cardID)) {
                hand.remove(i);
                cardToRemove = tempCard;
                break;
            }
        }

        if (cardToRemove == null) {
            throw new ClientException("cardID sent from client to remove from player's hand not found on server");
        }
        return cardToRemove;
    }

    public void addCardToDeck(ServerCard card) throws LogicException {
        deck.addCard(card);
    }

    private void setNextCard() {
        if (!deck.getCards().isEmpty()) {
            int index = new Random().nextInt(deck.getCards().size());
            nextCard = deck.getCards().get(index);
            try {
                deck.removeCard(nextCard);
            } catch (ClientException ignored) {
                System.out.println("Unable to remove card from deck");
            }
        } else {
            nextCard = null;
        }
    }

    public void setNewNextCard() {
        setNextCard();
    }

    boolean addNextCardToHand() {
        if (hand.size() < Constants.MAXIMUM_CARD_HAND_SIZE && (!deck.getCards().isEmpty() || nextCard != null)) {
            hand.add(nextCard);
            setNextCard();
            return true;
        }
        return false;
    }

    public ServerCard[] getCardsFromDeck(int cardsToDraw) {
        ServerCard[] drawnCards = new ServerCard[cardsToDraw];

        for(int i = 0; i < cardsToDraw; i++) {
            if (!deck.getCards().isEmpty()) {
                int index = new Random().nextInt(deck.getCards().size());
                ServerCard drawnCard = deck.getCards().get(index);
                drawnCards[i] = drawnCard;
                try {
                    deck.removeCard(drawnCard);
                } catch (ClientException ignored) {
                    System.out.println("Unable to remove card from deck");
                }
            } else {
                break;
            }
        }
        return drawnCards;
    }

    public void addCardsToHand(ServerCard... cards){
        for(ServerCard card : cards) {
            if (hand.size() < shared.Constants.MAXIMUM_CARD_HAND_SIZE && (!deck.getCards().isEmpty() || card != null)) {
                hand.add(card);
            }
        }
    }

    public String getUserName() {
        return this.userName;
    }

    public int getCurrentMP() {
        return this.currentMP;
    }

    void setCurrentMP(int currentMP) {
        this.currentMP = currentMP;
    }

    void increaseMP(int currentMP) {
        this.currentMP += currentMP;
    }

    void changeCurrentMP(int change) {
        currentMP += change;
    }

    public Deck getDeck() {
        return this.deck;
    }

    void addToGraveYard(ServerCard card) {
        graveyard.add(card);
    }

    public ServerTroop createHero() {
        if (hero == null) {
            hero = new ServerTroop(deck.getHero(), playerNumber);
            hero.setCanMove(true);
            hero.setCanAttack(true);
            troops.add(hero);
        }
        return hero;
    }

    public void setHero(ServerTroop hero) {
        this.hero = hero;
    }

    void killTroop(Game game, ServerTroop troop) {
        addToGraveYard(troop.getCard());
        troops.remove(troop);
        if (troop.getCard().getType().equals(CardType.HERO)) {
            hero = null;
        }
    }

    public MatchHistory getMatchHistory() {
        return matchHistory;
    }

    void setMatchHistory(MatchHistory matchHistory) {
        this.matchHistory = matchHistory;
    }

    public void addTroop(ServerTroop troop) {
        troops.add(troop);
    }

    public boolean getCanReplaceCard() {
        return getNumTimesReplacedThisTurn() < getMaxNumReplacePerTurn() && !deck.getCards().isEmpty();
    }

    public void setNumTimesReplacedThisTurn(int number) {
        this.numTimesReplacedThisTurn = number;
    }

    public int getNumTimesReplacedThisTurn() {
        return this.numTimesReplacedThisTurn;
    }

    public void setMaxNumReplacePerTurn(int number) {
        this.maxNumReplacePerTurn = number;
    }

    public int getMaxNumReplacePerTurn() {
        return this.maxNumReplacePerTurn;
    }

}