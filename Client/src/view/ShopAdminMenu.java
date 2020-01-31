package view;

import controller.GraphicalUserInterface;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import models.gui.*;

import java.io.File;
import java.io.FileNotFoundException;

class ShopAdminMenu extends Show {
    private static final String backgroundUrl = "Client/resources/menu/background/shop_admin_background.jpg";
    private static final EventHandler<? super MouseEvent> BACK_EVENT = event -> new MainMenu().show();
    private static Media backgroundMusic = new Media(
            new File("Client/resources/music/shop_menu.m4a").toURI().toString()
    );
    private static ShopAdminMenu menu;

    private ShopAdminMenu() throws FileNotFoundException {
        root.setBackground(UIConstants.DEFAULT_ROOT_BACKGROUND);
        BorderPane background = BackgroundMaker.getPlayBackground(backgroundUrl);
        BackButton backButton = new BackButton(BACK_EVENT);
        DefaultContainer container = new DefaultContainer(new ShopAdminList());

        AnchorPane sceneContents = new AnchorPane(background, container, backButton);

        root.getChildren().addAll(sceneContents);
    }

    public static ShopAdminMenu getInstance() {
        if (menu == null) {
            try {
                menu = new ShopAdminMenu();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return menu;
    }

    @Override
    public void show() {
        super.show();
        GraphicalUserInterface.getInstance().setBackgroundMusic(backgroundMusic);
    }
}
