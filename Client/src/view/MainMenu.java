package view;

import controller.*;
import javafx.application.Platform;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.media.Media;
import models.gui.*;
import models.message.OnlineGame;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static models.account.AccountType.ADMIN;

public class MainMenu extends Show {
    private static MainMenu menu;
    private static final Media backgroundMusic = new Media(
            new File("Client/resources/music/main_menu.m4a").toURI().toString()
    );
    private final List<MenuItem> items = new ArrayList<>();
    private int itemIndex = 0;
    private final MenuItem[] itemsArray = {
            new MenuItem(itemIndex++, "PLAY", "Single player, multiplayer", event -> PlayMenu.getInstance().show()),
            new MenuItem(itemIndex++, "PROFILE", "See you profile information", event -> menu.showProfileDialog()),
            new MenuItem(itemIndex++, "COLLECTION", "View your cards or build a deck", event -> new CollectionMenu().show()),
            new MenuItem(itemIndex++, "GLOBAL CHAT", "chat with other players", event -> GlobalChatDialog.getInstance().show()),
            new MenuItem(itemIndex++, "QUIT GAME", "Exit the game", event -> Client.getInstance().close())

    };

    private DialogContainer onlineGamesDialog;

    {
        items.addAll(Arrays.asList(itemsArray));
        if (Client.getInstance().getAccount().getAccountType() == ADMIN) {
            System.out.println(Client.getInstance().getAccount().getUsername());
            items.addAll(Arrays.asList(
                    new MenuItem(itemIndex++, "ONLINE GAMES", "View online games real time", event -> menu.showOnlineGamesList())
            ));
        }
    }

    public static MainMenu getInstance() {
        return menu;
    }

    public MainMenu() {
        menu = this;
        try {
            root.setBackground(UIConstants.DEFAULT_ROOT_BACKGROUND);
            BorderPane background = BackgroundMaker.getMenuBackground();
            MainMenuBox menuBox = new MainMenuBox(items);

            AnchorPane sceneContents = new AnchorPane(background, menuBox);
            root.getChildren().addAll(sceneContents);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void closeOnlineGamesList() {
        if (onlineGamesDialog != null) {
            onlineGamesDialog.close();
        }
    }

    private void showOnlineGamesList() {
        BackgroundMaker.makeMenuBackgroundFrozen();
        DialogBox dialogBox = new DialogBox();
        OnlineGamesList onlineGamesList = new OnlineGamesList();
        dialogBox.getChildren().add(onlineGamesList);
        onlineGamesDialog = new DialogContainer(root, dialogBox);
        dialogBox.makeClosable(onlineGamesDialog, closeEvent -> BackgroundMaker.makeMenuBackgroundUnfrozen());

        new Thread(() -> {
            try {
                OnlineGamesListController.getInstance().requestOnlineGamesList();
                synchronized (OnlineGamesListController.getInstance()) {
                    OnlineGamesListController.getInstance().wait();
                }
                OnlineGame[] onlineGames = OnlineGamesListController.getInstance().getOnlineGames();
                Platform.runLater(() -> onlineGamesList.setItems(onlineGames));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        onlineGamesDialog.show();
    }


    private void showProfileDialog() {
        BackgroundMaker.makeMenuBackgroundFrozen();
        GridPane profileGrid = new ProfileGrid(Client.getInstance().getAccount());

        DialogBox dialogBox = new DialogBox(profileGrid);
        DialogContainer dialogContainer = new DialogContainer(root, dialogBox);

        dialogContainer.show();

        dialogBox.makeButton("LOGOUT", event -> {
            dialogContainer.close();
            MainMenuController.getInstance().logout();
            new LoginMenu().show();

        });
        dialogBox.makeClosable(dialogContainer, closeEvent -> BackgroundMaker.makeMenuBackgroundUnfrozen());
    }

    @Override
    public void show() {
        super.show();
        BackgroundMaker.makeMenuBackgroundUnfrozen();
        GraphicalUserInterface.getInstance().setBackgroundMusic(backgroundMusic);
    }
}
