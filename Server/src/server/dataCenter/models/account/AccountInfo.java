package server.dataCenter.models.account;

import server.dataCenter.DataCenter;
import shared.models.account.BaseAccountInfo;

public class AccountInfo extends BaseAccountInfo {

  public AccountInfo(Account account) {
    super(account.getUsername(), DataCenter.getInstance().isOnline(account.getUsername()),
        account.getWins(), account.getAccountType());
  }
}
