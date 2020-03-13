package server.gameCenter.models.game;

import server.clientPortal.models.comperessedData.CompressedPlayer;
import server.dataCenter.models.account.MatchHistory;
import server.dataCenter.models.card.ServerCard;
import shared.Constants;
import shared.models.card.CardType;
import server.dataCenter.models.card.Deck;
import server.exceptions.ClientException;
import server.exceptions.LogicException;
import shared.models.game.map.Cell;

import java.util.*;

public class Player {
    private final String userName;
    private int currentMP;
    private final Deck deck;
    private ServerTroop hero;
    private final List<ServerCard> hand = new ArrayList<>();
    private final List<ServerTroop> troops = new ArrayList<>();
    private final List<ServerCard> graveyard = new ArrayList<>();
    private ServerCard nextCard;
    private final int playerNumber;
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

    public List<ServerCard> getHand() {
        return Collections.unmodifiableList(hand);
    }

    ServerCard insert(String cardId) throws ClientException {
        ServerCard card = null;
        Iterator iterator = hand.iterator();
        while (iterator.hasNext()) {
            ServerCard card1 = (ServerCard) iterator.next();
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

    void addToGraveYard(ServerCard card) {
        graveyard.add(card);
    }

    ServerCard getNextCard() {
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