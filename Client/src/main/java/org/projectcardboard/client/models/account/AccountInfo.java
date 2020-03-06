package org.projectcardboard.client.models.account;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class AccountInfo {
    private transient PropertyChangeSupport support = new PropertyChangeSupport(this);
    private String username;
    private boolean online;
    private int wins;
    private AccountType type;

    public String getUsername() {
        return username;
    }

    public boolean isOnline() {
        return online;
    }

    public int getWins() {
        return wins;
    }

    public AccountType getType() {
        return type;
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
