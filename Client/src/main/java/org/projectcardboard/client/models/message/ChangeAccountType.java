package org.projectcardboard.client.models.message;

import shared.models.account.AccountType;

public class ChangeAccountType {
  private final String username;
  private final AccountType newType;

  public ChangeAccountType(String username, AccountType newType) {
    this.username = username;
    this.newType = newType;
  }

  public String getUsername() {
    return username;
  }

  public AccountType getNewType() {
    return newType;
  }
}
