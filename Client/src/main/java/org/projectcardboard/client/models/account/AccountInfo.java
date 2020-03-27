package org.projectcardboard.client.models.account;

import shared.models.account.AccountType;
import shared.models.account.BaseAccountInfo;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class AccountInfo extends BaseAccountInfo { // Todo it seems that most of this class and its
                                                   // superclass are unused.
  private final transient PropertyChangeSupport support = new PropertyChangeSupport(this);

  public AccountInfo(String username, boolean online, int wins, AccountType type) {
    super(username, online, wins, type);
  }

  public void setType(AccountType newType) {
    AccountType old = type;
    this.type = newType;
    support.firePropertyChange("accountType", old, newType);
  }

  public void addListener(PropertyChangeListener pcl) {
    support.addPropertyChangeListener(pcl);
  }
}
