package org.projectcardboard.client.models.account;

import shared.models.account.BaseMatchHistory;

public class MatchHistory extends BaseMatchHistory {
    private MatchHistory(String oppName, boolean amIWinner) { //Unused.
        super(oppName, amIWinner);
    }
}