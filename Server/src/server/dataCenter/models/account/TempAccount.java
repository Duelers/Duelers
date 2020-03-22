package server.dataCenter.models.account;

import server.dataCenter.models.card.Deck;
import server.dataCenter.models.card.TempDeck;
import shared.models.account.AccountType;
import shared.models.account.BaseTempAccount;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TempAccount extends BaseTempAccount<Collection, TempDeck, MatchHistory> {

    public TempAccount(Account account) {
        super(account.getUsername(),
                account.getPassword(),
                account.getAccountType(),
                account.getCollection()
                );
        for (Deck deck : account.getDecks()) {
            this.decks.add(new TempDeck(deck));
        }
        if (account.getMainDeck() != null) {
            this.mainDeckName = account.getMainDeck().getName();
        }
    }

}
