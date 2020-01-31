package models.gui;

import javafx.scene.Node;
import javafx.scene.layout.BorderPane;

public class DefaultContainer extends BorderPane {

    public DefaultContainer(Node center) {
        setMinSize(UIConstants.SCENE_WIDTH, UIConstants.SCENE_HEIGHT);
        setCenter(center);
    }
}
