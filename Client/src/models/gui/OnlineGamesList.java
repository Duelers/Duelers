package models.gui;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import shared.models.game.GameType;
import models.message.OnlineGame;

import java.util.Arrays;
import java.util.stream.Collectors;

import static models.gui.UIConstants.SCALE;

public class OnlineGamesList extends TableView {

    public OnlineGamesList() {
        super();

        TableColumn<OnlineGameView, Integer> index = new TableColumn("Index");
        index.setSortable(false);
        index.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(getItems().indexOf(param.getValue()) + 1)
        );
        index.setMaxWidth(100 * SCALE);

        TableColumn<OnlineGameView, String> player1 = new TableColumn<>("Player 1");
        player1.setCellValueFactory(new PropertyValueFactory<>("player1"));
        player1.setMinWidth(300 * SCALE);

        TableColumn<OnlineGameView, String> player2 = new TableColumn<>("Player 2");
        player2.setCellValueFactory(new PropertyValueFactory<>("player2"));
        player2.setMinWidth(300 * SCALE);

        TableColumn<OnlineGameView, GameType> gameType = new TableColumn<>("Game type");
        gameType.setCellValueFactory(new PropertyValueFactory<>("gameType"));
        gameType.setMinWidth(300 * SCALE);

        TableColumn<OnlineGameView, Button> showButton = new TableColumn<>("");
        showButton.setCellValueFactory(new PropertyValueFactory<>("showButton"));
        showButton.setMinWidth(300 * SCALE);

        getColumns().addAll(index, player1, player2, gameType, showButton);
    }

    public void setItems(OnlineGame[] onlineGames) {
        setItems(
                FXCollections.observableArrayList(
                        Arrays.stream(onlineGames).map(OnlineGameView::new).collect(Collectors.toList())
                )
        );
    }
}
