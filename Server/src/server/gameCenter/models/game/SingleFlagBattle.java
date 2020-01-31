package server.gameCenter.models.game;

import server.dataCenter.models.account.Account;
import server.dataCenter.models.card.Card;
import server.dataCenter.models.card.Deck;
import server.exceptions.LogicException;
import server.exceptions.ServerException;
import server.gameCenter.models.map.GameMap;

public class SingleFlagBattle extends Game {
    private static final int DEFAULT_COMBO = 6;
    private int currentCombo = -1;

    public SingleFlagBattle(Account accountOne, Account accountTwo, GameMap gameMap) {
        super(accountOne, accountTwo.getMainDeck(), accountTwo.getUsername(), gameMap, GameType.A_FLAG);
    }

    public SingleFlagBattle(Account account1, Deck deck, GameMap gameMap) {
        super(account1, deck, "AI", gameMap, GameType.A_FLAG);
    }

    @Override
    public boolean finishCheck() {
        if (currentCombo >= DEFAULT_COMBO) {
            if (getPlayerOne().getNumberOfCollectedFlags() > 0) {
                setMatchHistories(true, false);
                finish();
                return true;
            } else if (getPlayerTwo().getNumberOfCollectedFlags() > 0) {
                setMatchHistories(false, true);
                finish();
                return true;
            }
        }
        return false;
    }

    @Override
    public void changeTurn(String username) throws LogicException {
        super.changeTurn(username);
        increaseCombo();
    }

    @Override
    void catchFlag(Troop troop, Card item) throws ServerException {
        super.catchFlag(troop, item);
        currentCombo = 0;
    }

    @Override
    void killTroop(Troop troop) {
        if (troop.getFlags().size() > 0) {
            currentCombo = -1;
        }
        super.killTroop(troop);
    }

    private void increaseCombo() {
        if (currentCombo >= 0) {
            currentCombo++;
        }
    }
}
