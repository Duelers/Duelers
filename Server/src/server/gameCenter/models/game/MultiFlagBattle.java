package server.gameCenter.models.game;

import server.dataCenter.models.account.Account;
import server.dataCenter.models.card.Deck;
import server.gameCenter.models.map.GameMap;

public class MultiFlagBattle extends Game {
    private int numberOfFlags;

    public MultiFlagBattle(Account accountOne, Account accountTwo, GameMap gameMap, int numberOfFlags) {
        super(accountOne, accountTwo.getMainDeck(), accountTwo.getUsername(), gameMap, GameType.SOME_FLAG);
        this.numberOfFlags = numberOfFlags;
    }

    public MultiFlagBattle(Account account1, Deck deck, GameMap gameMap, int numberOfFlags) {
        super(account1, deck, "AI", gameMap, GameType.SOME_FLAG);
        this.numberOfFlags = numberOfFlags;
    }

    @Override
    public boolean finishCheck() {
        if (getPlayerOne().getNumberOfCollectedFlags() >= numberOfFlags / 2) {
            setMatchHistories(true, false);
            finish();
            return true;
        }
        if (getPlayerTwo().getNumberOfCollectedFlags() >= numberOfFlags / 2) {
            setMatchHistories(false, true);
            finish();
            return true;
        }
        return false;
    }
}
