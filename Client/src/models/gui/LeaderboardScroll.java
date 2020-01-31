package models.gui;

import controller.Client;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.shape.Circle;
import models.account.AccountInfo;
import models.account.AccountType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static models.account.AccountType.ADMIN;
import static models.gui.UIConstants.SCALE;

public class LeaderboardScroll extends TableView {
    public LeaderboardScroll(List<LeaderBoardView> leaderboard) {
        super(FXCollections.observableArrayList(leaderboard));

        TableColumn<LeaderBoardView, Integer> index = new TableColumn<>("Rank");
        index.setSortable(false);
        index.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(getItems().indexOf(param.getValue()) + 1)
        );
        index.setMaxWidth(100 * SCALE);

        TableColumn<LeaderBoardView, String> username = new TableColumn<>("Username");
        username.setCellValueFactory(new PropertyValueFactory<>("username"));
        username.setMinWidth(300 * SCALE);

        TableColumn<LeaderBoardView, String> wins = new TableColumn<>("Wins");
        wins.setCellValueFactory(new PropertyValueFactory<>("wins"));
        wins.setMaxWidth(100 * SCALE);

        TableColumn<LeaderBoardView, Circle> online = new TableColumn<>("Online");
        online.setCellValueFactory(param -> param.getValue().onlineViewProperty());
        online.setCellFactory(cell -> new CircleTableCell());
        online.setMaxWidth(100 * SCALE);

        getColumns().addAll(index, username, wins, online);

        if (Client.getInstance().getAccount().getAccountType() == ADMIN) {
            TableColumn<LeaderBoardView, Spinner<AccountType>> accountType = new TableColumn<>("Account Type");
            accountType.setCellValueFactory(new PropertyValueFactory<>("typeSpinner"));
            accountType.setMinWidth(300 * SCALE);
            getColumns().add(accountType);
        }
    }

    public void setItems(AccountInfo[] leaderboard) {
        setItems(
                FXCollections.observableArrayList(
                        Arrays.stream(leaderboard).map(LeaderBoardView::new).collect(Collectors.toList())
                )
        );
    }

    private class CircleTableCell extends TableCell<LeaderBoardView, Circle> {
        @Override
        protected void updateItem(Circle item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setGraphic(null);
            } else {
                setGraphic(item);
            }
        }
    }
}
