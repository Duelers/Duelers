package server.gameCenter.models.game;

import server.clientPortal.models.comperessedData.CompressedCard;
import server.clientPortal.models.comperessedData.CompressedPlayer;
import server.dataCenter.models.account.MatchHistory;
import server.dataCenter.models.card.Card;
import server.dataCenter.models.card.CardType;
import server.dataCenter.models.card.Deck;
import server.exceptions.ClientException;
import server.gameCenter.models.map.Cell;

import java.util.*;

public class Player {
    private String userName;
    private int currentMP;
    private Deck deck;
    private Troop hero;
    private List<Card> hand = new ArrayList<>();
    private List<Troop> troops = new ArrayList<>();
    private List<Card> graveyard = new ArrayList<>();
    private Card nextCard;
    private List<Card> collectedItems = new ArrayList<>();
    private List<Troop> flagCarriers = new ArrayList<>();
    private int playerNumber;
    private int numberOfCollectedFlags;
    private MatchHistory matchHistory;

    Player(Deck mainDeck, String userName, int playerNumber) {
        this.playerNumber = playerNumber;
        this.userName = userName;
        deck = new Deck(mainDeck);
        setNextCard();
        for (int i = 0; i < 3; i++) {
            addNextCardToHand();
        }
    }

    public CompressedPlayer toCompressedPlayer() {
        return new CompressedPlayer(
                userName, currentMP, hand, graveyard, nextCard,deck.getItem().toCompressedCard(), collectedItems, playerNumber, numberOfCollectedFlags);
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

        if (card == null) {
            iterator = collectedItems.iterator();
            while (iterator.hasNext()) {
                Card card1 = (Card) iterator.next();
                if (card1.getCardId().equalsIgnoreCase(cardId)) {
                    card = card1;
                    break;
                }
            }
        }

        if (card == null)
            throw new ClientException("card id is not valid");

        if (card.getMannaPoint() > currentMP)
            throw new ClientException("not enough manna point");

        iterator.remove();
        currentMP -= card.getMannaPoint();

        return card;
    }

    private void setNextCard() {
        if (!deck.getOthers().isEmpty()) {
            int index = new Random().nextInt(deck.getOthers().size());
            nextCard = deck.getOthers().get(index);
            try {
                deck.removeCard(nextCard);
            } catch (ClientException ignored) {
            }
        }

    }

    boolean addNextCardToHand() {
        if (hand.size() < 5) {
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

    void increaseMP(int currentMP){
        this.currentMP+= currentMP;
    }

    void addFlagCarrier(Troop troop) {
        if (!this.flagCarriers.contains(troop))
            this.flagCarriers.add(troop);
    }

    public void removeFlagCarrier(Troop troop) {
        flagCarriers.remove(troop);
    }

    void changeCurrentMP(int change) {
        currentMP += change;
    }

    int getPlayerNumber() {
        return playerNumber;
    }

    public Deck getDeck() {
        return this.deck;
    }

    public List<Troop> getTroops() {
        return Collections.unmodifiableList(troops);
    }

    void addToGraveYard(Card card) {
        graveyard.add(card);
    }

    Card getNextCard() {
        return this.nextCard;
    }

    public List<Card> getCollectedItems() {
        return Collections.unmodifiableList(collectedItems);
    }

    void collectItem(Card card) {
        collectedItems.add(card);
    }

    Troop getTroop(Cell cell) {
        for (Troop troop : troops) {
            if (troop.getCell().equals(cell)) {
                return troop;
            }
        }
        return null;
    }

    Troop getTroop(String cardId) {
        for (Troop troop : troops) {
            if (troop.getCard().getCardId().equalsIgnoreCase(cardId)) {
                return troop;
            }
        }
        return null;
    }

    public Troop createHero() {
        if (hero == null) {
            hero = new Troop(deck.getHero(), playerNumber);
            hero.setCanMove(true);
            hero.setCanAttack(true);
            troops.add(hero);
        }
        return hero;
    }

    public Troop getHero() {
        return hero;
    }

    public void setHero(Troop hero) {
        this.hero = hero;
    }

    void killTroop(Game game, Troop troop) {
        addToGraveYard(troop.getCard());
//        Server.getInstance().sendChangeCardPositionMessage(game, troop.getCard(), CardPosition.GRAVE_YARD);
        troops.remove(troop);
        if (troop.getCard().getType() == CardType.HERO) {
            hero = null;
        }
    }

    public int getNumberOfCollectedFlags() {
        return numberOfCollectedFlags;
    }

    void increaseNumberOfCollectedFlags() {
        this.numberOfCollectedFlags++;
    }

    public void decreaseNumberOfCollectedFlags() {
        this.numberOfCollectedFlags--;
    }

    public MatchHistory getMatchHistory() {
        return matchHistory;
    }

    void setMatchHistory(MatchHistory matchHistory) {
        this.matchHistory = matchHistory;
    }

    void addTroop(Troop troop) {
        troops.add(troop);
    }
}