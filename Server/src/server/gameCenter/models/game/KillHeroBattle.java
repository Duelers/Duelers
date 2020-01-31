package server.gameCenter.models.game;

import server.dataCenter.models.account.Account;
import server.dataCenter.models.card.Deck;
import server.gameCenter.models.map.GameMap;

public class KillHeroBattle extends Game {

    public KillHeroBattle(Account account1, Account account2, GameMap gameMap) {
        super(account1, account2.getMainDeck(), account2.getUsername(), gameMap, GameType.KILL_HERO);
    }

    public KillHeroBattle(Account account1, Deck deck, GameMap gameMap) {
        super(account1, deck, "AI", gameMap, GameType.KILL_HERO);
    }

    @Override
    public boolean finishCheck() {
        if ((getPlayerOne().getHero() == null || getPlayerOne().getHero().getCurrentHp() <= 0) && (getPlayerTwo().getHero() == null || getPlayerTwo().getHero().getCurrentHp() <= 0)) {
            setMatchHistories(true, true);
            finish();
            return true;

        }
        if (getPlayerOne().getHero() == null || getPlayerOne().getHero().getCurrentHp() <= 0) {
            setMatchHistories(false, true);
            finish();
            return true;

        }
        if (getPlayerTwo().getHero() == null || getPlayerTwo().getHero().getCurrentHp() <= 0) {
            setMatchHistories(true, false);
            finish();
            return true;
        }
        return false;
    }
}
