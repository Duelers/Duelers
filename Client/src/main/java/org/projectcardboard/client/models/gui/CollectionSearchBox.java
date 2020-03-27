package org.projectcardboard.client.models.gui;

import org.projectcardboard.client.controller.CollectionMenuController;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;

import java.util.concurrent.atomic.AtomicReference;

public class CollectionSearchBox extends HBox {

    private final SearchField searchField;

    public CollectionSearchBox() {
        super(UIConstants.DEFAULT_SPACING * 2);
        setAlignment(Pos.CENTER);

        searchField = new SearchField();

        AtomicReference<String> oldValue = new AtomicReference<>("");
        searchField.setOnKeyPressed(event -> {
            if (searchField.getText().equals(oldValue.get())) return;

            oldValue.set(searchField.getText());
            CollectionMenuController.getInstance().search(searchField.getText());
        });

        getChildren().addAll(searchField);
    }

    public void clear() {
        searchField.clear();
        CollectionMenuController.getInstance().search("");
    }
}
