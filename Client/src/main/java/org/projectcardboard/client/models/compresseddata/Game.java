package org.projectcardboard.client.models.compresseddata;

import org.projectcardboard.client.models.game.Player;
import org.projectcardboard.client.models.game.map.GameMap;
import shared.models.card.CardType;
import shared.models.card.Card;
import shared.models.game.BaseGame;
import shared.models.game.Troop;
import shared.models.game.map.CellEffect;
import shared.models.game.GameType;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class Game extends BaseGame<Player, GameMap> {
    private transient PropertyChangeSupport support = new PropertyChangeSupport(this);


    public Game(Player playerOne, Player playerTwo, GameMap gameMap, int turnNumber, GameType gameType) {
        super(playerOne, playerTwo, gameMap, turnNumber, gameType);
    }

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
        Player player = getCurrentTurnPlayer();
        player.addNextCardToHand();
        player.removeCardFromNext();
    }

    public void moveCardToNext(Card card) {
        Player player = getCurrentTurnPlayer();
        player.addCardToNext(card);
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
                System.out.println("Client error, troop in moveCardToGraveYard is null");
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


}
