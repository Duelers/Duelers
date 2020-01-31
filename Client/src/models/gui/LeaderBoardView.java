package models.gui;

import controller.Client;
import controller.MainMenuController;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.Spinner;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import models.account.AccountInfo;
import models.account.AccountType;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

import static models.gui.UIConstants.SCALE;

public class LeaderBoardView implements PropertyChangeListener {
    private static final Map<Boolean, Color> colors = new HashMap<>();
    private static final double circleRadius = 15 * SCALE;

    static {
        colors.put(true, Color.rgb(0, 234, 0));
        colors.put(false, Color.rgb(255, 0, 0));
    }

    private final String username;
    private final int wins;
    private final ObjectProperty<Circle> onlineView;
    private final Spinner<AccountType> typeSpinner = new Spinner<>(FXCollections.observableArrayList(AccountType.values()));

    public LeaderBoardView(AccountInfo accountInfo) {
        username = accountInfo.getUsername();
        wins = accountInfo.getWins();
        onlineView = new SimpleObjectProperty<>(new Circle(circleRadius, colors.get(accountInfo.isOnline())));

        typeSpinner.getValueFactory().setValue(accountInfo.getType());

        typeSpinner.valueProperty().addListener(((observable, oldValue, newValue) -> {
            if (!accountInfo.getType().equals(newValue)) {
                System.out.println(newValue);
                typeSpinner.getValueFactory().setValue(oldValue);
                MainMenuController.getInstance().changeAccountTypeRequest(accountInfo.getUsername(), newValue);
            }
        }));
        typeSpinner.getValueFactory().setWrapAround(true);
        if (accountInfo.getUsername().equals(Client.getInstance().getAccount().getUsername())) {
            typeSpinner.setVisible(false);
        } else {
            accountInfo.addListener(this);
        }
    }

    public String getUsername() {
        return username;
    }

    public int getWins() {
        return wins;
    }

    public Circle getOnlineView() {
        return onlineView.get();
    }

    ObjectProperty<Circle> onlineViewProperty() {
        return onlineView;
    }

    public Spinner<AccountType> getTypeSpinner() {
        return typeSpinner;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("accountType".equals(evt.getPropertyName())) {
            Platform.runLater(() -> typeSpinner.getValueFactory().setValue((AccountType) evt.getNewValue()));
        }
    }
}
