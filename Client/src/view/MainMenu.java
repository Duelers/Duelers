package view;

import controller.*;
import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.media.Media;
import models.account.AccountInfo;
import models.gui.*;
import models.message.OnlineGame;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static models.account.AccountType.ADMIN;

public class MainMenu extends Show {
    private static MainMenu menu;
    private static Media backgroundMusic = new Media(
            new File("Client/resources/music/main_menu.m4a").toURI().toString()
    );
    private final List<MenuItem> items = new ArrayList<>();
    private int itemIndex = 0;
    private boolean inLeaderBoard = false;
    private boolean inOnlineGames = false;
    private final MenuItem[] itemsArray = {
            new MenuItem(itemIndex++, "PLAY", "Single player, multiplayer", event -> PlayMenu.getInstance().show()),
            new MenuItem(itemIndex++, "PROFILE", "See you profile information", event -> menu.showProfileDialog()),
            //disabled for now. General consensus is that we do not want it
            //new MenuItem(itemIndex++, "SHOP", "Buy or sell every card you want", event -> new ShopMenu().show()),
            new MenuItem(itemIndex++, "COLLECTION", "View your cards or build a deck", event -> new CollectionMenu().show()),
            new MenuItem(itemIndex++, "CUSTOM CARD", "Design your card with your own taste", event -> new CustomCardMenu().show()),
            new MenuItem(itemIndex++, "GLOBAL CHAT", "chat with other players", event -> GlobalChatDialog.getInstance().show()),
            new MenuItem(itemIndex++, "LEADERBOARD", "See other people and their place", event -> menu.showLeaderboard()),
            new MenuItem(itemIndex++, "QUIT GAME", "Exit the game", event -> System.exit(0))

    };
    private DialogContainer onlineGamesDialog;

    {
        items.addAll(Arrays.asList(itemsArray));
        if (Client.getInstance().getAccount().getAccountType() == ADMIN) {
            System.out.println(Client.getInstance().getAccount().getUsername());
            items.addAll(Arrays.asList(
                    new MenuItem(itemIndex++, "SHOP ADMIN", "Change shop properties", event -> ShopAdminMenu.getInstance().show()),
                    new MenuItem(itemIndex++, "CUSTOM CARD REQUESTS", "check custom card requests", event -> menu.showCustomCardRequests()),
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
            showGlobalChatDialog(sceneContents);

            root.getChildren().addAll(sceneContents);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void showCustomCardRequests() {
        BackgroundMaker.makeMenuBackgroundFrozen();
        DialogBox dialogBox = new DialogBox();
        CustomCardRequestsList requestsList = new CustomCardRequestsList();
        dialogBox.getChildren().add(requestsList);
        DialogContainer customCardRequestsDialog = new DialogContainer(root, dialogBox);
        dialogBox.makeClosable(customCardRequestsDialog, closeEvent -> {
            CustomCardRequestsController.getInstance().removeListener(requestsList);
            BackgroundMaker.makeMenuBackgroundUnfrozen();
        });

        CustomCardRequestsController.getInstance().requestCustomCardRequests();

        customCardRequestsDialog.show();
    }

    private void showGlobalChatDialog(AnchorPane sceneContents) {
        sceneContents.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.T)) {
                GlobalChatDialog.getInstance().show();
            }
        });
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
        dialogBox.makeClosable(onlineGamesDialog, closeEvent -> {
            BackgroundMaker.makeMenuBackgroundUnfrozen();
            inOnlineGames = false;
        });

        inOnlineGames = true;
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

    private void showLeaderboard() {
        BackgroundMaker.makeMenuBackgroundFrozen();
        DialogBox dialogBox = new DialogBox();
        LeaderboardScroll leaderboardScroll = new LeaderboardScroll(Collections.emptyList());
        dialogBox.getChildren().add(leaderboardScroll);

        inLeaderBoard = true;
        new Thread(() -> {
            try {
                while (inLeaderBoard) {
                    MainMenuController.getInstance().requestLeaderboard();
                    synchronized (MainMenuController.getInstance()) {
                        MainMenuController.getInstance().wait();
                    }
                    AccountInfo[] leaderboard = MainMenuController.getInstance().getLeaderBoard();
                    Platform.runLater(() -> leaderboardScroll.setItems(leaderboard));
                    Thread.sleep(4000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        DialogContainer dialogContainer = new DialogContainer(root, dialogBox);
        dialogContainer.show();
        dialogBox.makeClosable(dialogContainer, closeEvent -> {
            BackgroundMaker.makeMenuBackgroundUnfrozen();
            inLeaderBoard = false;
        });
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
