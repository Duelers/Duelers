package view;

import controller.GraphicalUserInterface;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import models.gui.BackgroundMaker;
import models.gui.DefaultContainer;
import models.gui.LoginMenuContainer;
import models.gui.UIConstants;

import java.io.FileNotFoundException;

public class LoginMenu extends Show {

    public LoginMenu() {
        try {
            root.setBackground(UIConstants.DEFAULT_ROOT_BACKGROUND);

            BorderPane background = BackgroundMaker.getMenuBackground();
            DefaultContainer container = new DefaultContainer(new LoginMenuContainer());

            AnchorPane sceneContents = new AnchorPane(background, container);

            root.getChildren().addAll(sceneContents);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void show() {
        super.show();
        BackgroundMaker.makeMenuBackgroundFrozen();
        GraphicalUserInterface.getInstance().stopBackgroundMusic();
    }
}