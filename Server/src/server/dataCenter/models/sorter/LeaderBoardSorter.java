package server.dataCenter.models.sorter;

import server.dataCenter.models.account.Account;

import java.util.Comparator;

public class LeaderBoardSorter implements Comparator<Account> {
    @Override
    public int compare(Account o1, Account o2) {
        if (o1.getWins() != o2.getWins()) {
            return o2.getWins() - o1.getWins();
        }
        return o1.getUsername().compareTo(o2.getUsername());
    }
}
