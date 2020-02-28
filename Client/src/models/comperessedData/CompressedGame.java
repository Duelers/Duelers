package models.comperessedData;

import models.card.CardType;
import shared.models.game.map.CellEffect;
import shared.models.game.GameType;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class CompressedGame {
    private CompressedPlayer playerOne;
    private CompressedPlayer playerTwo;
    private CompressedGameMap gameMap;
    private int turnNumber;
    private GameType gameType;

    private PropertyChangeSupport support = new PropertyChangeSupport(this);

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        if (support == null) {
            support = new PropertyChangeSupport(this);
        }
        support.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }

    public void moveCardToHand() {
        CompressedPlayer player = getCurrentTurnPlayer();
        player.addNextCardToHand();
        player.removeCardFromNext();
    }

    public void moveCardToNext(CompressedCard card) {
        CompressedPlayer player = getCurrentTurnPlayer();
        player.addCardToNext(card);
    }

    public void moveCardToMap(CompressedCard card) {
        CompressedPlayer player = getCurrentTurnPlayer();
        player.removeCardFromHand(card.getCardId());
    }

    public void moveCardToGraveYard(CompressedCard card) {
        CompressedPlayer player;
        if (card.getType() == CardType.HERO || card.getType() == CardType.MINION) {
            CompressedTroop troop = gameMap.getTroop(card.getCardId());
            if (troop == null) {
                System.out.println("Client Game Error!!");
            } else {
                player = getPlayer(troop.getPlayerNumber());
                player.removeTroop(card.getCardId());
                player.addCardToGraveYard(card);
                gameMap.killTroop(card.getCardId());
            }
        } else {
            player = getCurrentTurnPlayer();
            player.removeCardFromHand(card.getCardId());
            player.addCardToGraveYard(card);
        }
    }

    public void troopUpdate(CompressedTroop troop) {
        CompressedPlayer player;
        player = getPlayer(troop.getPlayerNumber());
        if (player.searchGraveyard(troop.getCard().getCardId()) == null) {
            player.troopUpdate(troop);
            gameMap.updateTroop(troop);
        }
    }

    public void gameUpdate(int turnNumber, int player1CurrentMP,
                           int player2CurrentMP, CellEffect[] cellEffects) {
        int maxMP = 9;
        if (turnNumber < 14)
            maxMP = turnNumber / 2 + 2;
        if (support == null) {
            support = new PropertyChangeSupport(this);
        }
        if (this.turnNumber != turnNumber) {
            support.firePropertyChange("turn", this.turnNumber, turnNumber);
            this.turnNumber = turnNumber;
            support.firePropertyChange("mp1", player1CurrentMP, maxMP);
            playerOne.setCurrentMP(player1CurrentMP, turnNumber);
            support.firePropertyChange("mp2", player2CurrentMP, maxMP);
            playerTwo.setCurrentMP(player2CurrentMP, turnNumber);
        }
        if (playerOne.getCurrentMP() != player1CurrentMP) {
            support.firePropertyChange("mp1", player1CurrentMP, maxMP);
            playerOne.setCurrentMP(player1CurrentMP, turnNumber);
        }

        if (playerTwo.getCurrentMP() != player2CurrentMP) {
            support.firePropertyChange("mp2", player2CurrentMP, maxMP);
            playerTwo.setCurrentMP(player2CurrentMP, turnNumber);
        }
        gameMap.updateCellEffects(cellEffects);
    }

    public CompressedPlayer getPlayerOne() {
        return playerOne;
    }

    public CompressedPlayer getPlayerTwo() {
        return playerTwo;
    }

    public CompressedGameMap getGameMap() {
        return gameMap;
    }

    public int getTurnNumber() {
        return turnNumber;
    }

    public GameType getGameType() {
        return gameType;
    }

    public CompressedPlayer getCurrentTurnPlayer() {
        return getPlayer(turnNumber % 2);
    }

    public CompressedPlayer getOtherTurnPlayer() {
        return getPlayer(turnNumber % 2 + 1);
    }

    private CompressedPlayer getPlayer(int number) {
        if (number == 1) {
            return playerOne;
        } else {
            return playerTwo;
        }
    }

}
