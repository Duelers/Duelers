package server.gameCenter.models.game;

import server.clientPortal.models.comperessedData.CompressedPlayer;
import server.dataCenter.models.account.MatchHistory;
import shared.models.card.Card;
import shared.models.card.CardType;
import server.dataCenter.models.card.Deck;
import server.exceptions.ClientException;
import server.exceptions.LogicException;
import shared.models.game.ServerTroop;
import shared.models.game.ServerTroop;
import shared.models.game.map.Cell;
import server.dataCenter.models.Constants;

import java.util.*;

public class Player {
    private String userName;
    private int currentMP;
    private Deck deck;
    private ServerTroop hero;
    private List<Card> hand = new ArrayList<>();
    private List<ServerTroop> troops = new ArrayList<>();
    private List<Card> graveyard = new ArrayList<>();
    private Card nextCard;
    private int playerNumber;
    private MatchHistory matchHistory;
    private int numTimesReplacedThisTurn;
    private int maxNumReplacePerTurn;

    Player(Deck mainDeck, String userName, int playerNumber) {
        this.playerNumber = playerNumber;
        this.userName = userName;
        deck = new Deck(mainDeck);
        setNextCard();
        for (int i = 0; i < 3; i++) {
            addNextCardToHand();
        }
        this.numTimesReplacedThisTurn = 0;
        this.maxNumReplacePerTurn = 1;
    }

    public CompressedPlayer toCompressedPlayer() {
        return new CompressedPlayer(
                userName, currentMP, hand, graveyard, nextCard, playerNumber);
    }

    public List<Card> getHand() {
        return Collections.unmodifiableList(hand);
    }

    Card insert(String cardId) throws ClientException {
        Card card = null;
        Iterator iterator = hand.iterator();
        while (iterator.hasNext()) {
            Card card1 = (Card) iterator.next();
            if (card1.getCardId().equalsIgnoreCase(cardId)) {
                card = card1;
                break;
            }
        }

        if (card == null)
            throw new ClientException("card id is not valid");

        if (card.getManaCost() > currentMP)
            throw new ClientException("not enough mana");

        iterator.remove();
        currentMP -= card.getManaCost();

        return card;
    }

    public Card removeCardFromHand(String cardID) throws ClientException {
        Card cardToRemove = null;

        for (int i = 0; i < hand.size(); i++) {
            Card tempCard = hand.get(i);
            if (tempCard.getCardId().equalsIgnoreCase(cardID)) {
                hand.remove(i);
                cardToRemove = tempCard;
            }
        }

        if (cardToRemove == null) {
            throw new ClientException("cardID sent from client to remove from player's hand not found on server");
        }
        return cardToRemove;
    }

    public void addCardToDeck(Card card) throws LogicException {
        deck.addCard(card);
    }

    private void setNextCard() {
        if (!deck.getOthers().isEmpty()) {
            int index = new Random().nextInt(deck.getOthers().size());
            nextCard = deck.getOthers().get(index);
            try {
                deck.removeCard(nextCard);
            } catch (ClientException ignored) {
                System.out.println("Unable to remove card from deck");
            }
        }

    }

    public void setNewNextCard() {
        setNextCard();
    }

    boolean addNextCardToHand() {
        if (hand.size() < Constants.MAXIMUM_CARD_HAND_SIZE && !deck.getOthers().isEmpty()) {
            hand.add(nextCard);
            setNextCard();
            return true;
        }
        return false;
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

    public int getPlayerNumber() {
        return playerNumber;
    }

    public Deck getDeck() {
        return this.deck;
    }

    public List<ServerTroop> getTroops() {
        return Collections.unmodifiableList(troops);
    }

    void addToGraveYard(Card card) {
        graveyard.add(card);
    }

    Card getNextCard() {
        return this.nextCard;
    }

    ServerTroop getTroop(Cell cell) {
        for (ServerTroop troop : troops) {
            if (troop.getCell().equals(cell)) {
                return troop;
            }
        }
        return null;
    }

    ServerTroop getTroop(String cardId) {
        for (ServerTroop troop : troops) {
            if (troop.getCard().getCardId().equalsIgnoreCase(cardId)) {
                return troop;
            }
        }
        return null;
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

    public ServerTroop getHero() {
        return hero;
    }

    public void setHero(ServerTroop hero) {
        this.hero = hero;
    }

    void killTroop(Game game, ServerTroop troop) {
        addToGraveYard(troop.getCard());
//        Server.getInstance().sendChangeCardPositionMessage(game, troop.getCard(), CardPosition.GRAVE_YARD);
        troops.remove(troop);
        if (troop.getCard().getType() == CardType.HERO) {
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
        return getNumTimesReplacedThisTurn() < getMaxNumReplacePerTurn();
    }

    public void setNumTimesReplacedThisTurn(int number){
        this.numTimesReplacedThisTurn = number;
    }

    public int getNumTimesReplacedThisTurn(){
        return this.numTimesReplacedThisTurn;
    }

    public void setMaxNumReplacePerTurn(int number){
        this.maxNumReplacePerTurn = number;
    }

    public int getMaxNumReplacePerTurn(){
        return this.maxNumReplacePerTurn;
    }
}