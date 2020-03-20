package org.projectcardboard.client.models.compresseddata;

import org.projectcardboard.client.models.game.Player;
import org.projectcardboard.client.models.game.map.GameMap;
import shared.models.card.CardType;
import shared.models.card.Card;
import shared.models.game.Troop;
import shared.models.game.map.CellEffect;
import shared.models.game.GameType;
import shared.models.services.Log;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.logging.Level;

public class CompressedGame {
    private Player playerOne;
    private Player playerTwo;
    private GameMap gameMap;
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

    public void moveCardToMap(Card card) {
        Player player = getCurrentTurnPlayer();
        player.removeCardFromHand(card.getCardId());
    }

    public void moveCardToGraveYard(Card card) {
        Player player;
        if (card.getType().equals(CardType.HERO) || card.getType().equals(CardType.MINION)) {
            Troop troop = gameMap.getTroop(card.getCardId());
            if (troop == null) {
                Log.getInstance().logClientData("troop in moveCardToGraveYard is null", Level.SEVERE);
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

    public void troopUpdate(Troop troop) {
        Player player;
        player = getPlayer(troop.getPlayerNumber());
        if (player.searchGraveyard(troop.getCard().getCardId()) == null) { //Todo, this looks avoidable.
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

    public Player getPlayerOne() {
        return playerOne;
    }

    public Player getPlayerTwo() {
        return playerTwo;
    }

    public GameMap getGameMap() {
        return gameMap;
    }

    public int getTurnNumber() {
        return turnNumber;
    }

    public GameType getGameType() {
        return gameType;
    }

    public Player getCurrentTurnPlayer() {
        return getPlayer(turnNumber % 2);
    }

    public Player getOtherTurnPlayer() {
        return getPlayer(turnNumber % 2 + 1);
    }

    private Player getPlayer(int number) {
        if (number == 1) {
            return playerOne;
        } else {
            return playerTwo;
        }
    }

}
