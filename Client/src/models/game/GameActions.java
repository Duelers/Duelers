package models.game;

import models.comperessedData.CompressedCard;
import models.comperessedData.CompressedTroop;
import models.message.OnlineGame;

import java.util.ArrayList;

public interface GameActions {

    void attack(CompressedTroop selectedTroop, CompressedTroop troop);

    void comboAttack(ArrayList<CompressedTroop> comboTroops, CompressedTroop troop);

    void move(CompressedTroop selectedTroop, int j, int i);

    void endTurn();

    void forceFinish();

    void insert(CompressedCard card, int row, int column);

    void exitGameShow(OnlineGame onlineGame);
}
