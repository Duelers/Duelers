package org.projectcardboard.client.models.gui;

import javafx.collections.FXCollections;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

public class DeckListView extends ListView<String> {
    public DeckListView(String[] deckNames) {
        super(FXCollections.observableArrayList(deckNames));
        setCellFactory(param -> new ListCell<>() {
            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    setText(item);
                    setFont(UIConstants.DEFAULT_FONT);
                } else {
                    setText(null);
                }
            }
        });
    }
}
