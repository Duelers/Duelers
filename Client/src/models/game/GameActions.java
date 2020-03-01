package models.game;

import models.message.OnlineGame;
import shared.models.card.Card;
import shared.models.card.CompressedTroop;


public interface GameActions {

    void attack(CompressedTroop selectedTroop, CompressedTroop troop);

    void move(CompressedTroop selectedTroop, int j, int i);

    void endTurn();

    void forceFinish();

    void insert(Card card, int row, int column);

    void exitGameShow(OnlineGame onlineGame);

    void setNewNextCard();

    void replaceCard(String cardID);
}
