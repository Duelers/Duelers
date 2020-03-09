package org.projectcardboard.client.models.game;

import org.projectcardboard.client.models.message.OnlineGame;
import shared.models.card.Card;
import shared.models.game.Troop;


public interface GameActions {

    void attack(Troop selectedTroop, Troop troop);

    void move(Troop selectedTroop, int j, int i);

    void endTurn();

    void forceFinish();

    void insert(Card card, int row, int column);

    void exitGameShow(OnlineGame onlineGame);

    void setNewNextCard();

    void replaceCard(String cardID);

    void getCurrentDeckSize();
}
