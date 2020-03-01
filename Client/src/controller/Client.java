package controller;

import Config.Config;
import com.google.gson.Gson;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import javafx.application.Platform;
import models.account.Account;
import models.message.CardPosition;
import models.message.GameUpdateMessage;
import models.message.Message;
import view.BattleView.BattleScene;
import view.*;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;


public class Client {
    private static WebSocket ws;
    private static Client client;
    private final LinkedList<Message> sendingMessages = new LinkedList<>();
    private static String clientName;
    private static Account account;
    private static Show currentShow;
    private final Gson gson = new Gson();
    private static Thread sendMessageThread;


    public static Client getInstance() {
        if (client == null) {
            client = new Client();
        }
        return client;
    }

    private void connect() throws IOException, NullPointerException {
        String serverIP = Config.getInstance().getProperty("SERVER_IP");
        String port = Config.getInstance().getProperty("PORT");

        sendMessageThread = new Thread(() -> {
            try {
                sendMessages();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        ws = new WebSocketFactory().createSocket("ws://" + serverIP + ":" + port + "/websockets/game");
        ws.addListener(new WebSocketAdapter() {
            @Override
            public void onTextMessage(WebSocket websocket, String message) throws Exception {
                Message messageObject = gson.fromJson(message, Message.class);

                String msg = simplifyLogMessage(messageObject, "Server");
                if (msg != null) {
                    System.out.println(msg);
                } else {
                    System.out.println(message);
                }
                handleMessage(messageObject);
            }
        });
        try {
            ws.connect();
        } catch (WebSocketException e) {
            throw new RuntimeException(e);
        }
        sendMessageThread.start();
    }

    void addToSendingMessagesAndSend(Message message) {
        synchronized (sendingMessages) {
            sendingMessages.add(message);
            sendingMessages.notify();
        }
    }

    private String simplifyLogMessage(Message message, String sender){

        final String header = String.format("%s says: ", sender);

        switch (message.getMessageType()) {
            case TROOP_UPDATE:

                int currentAttack = message.getTroopUpdateMessage().getCompressedTroop().getCurrentAp();
                int currentHealth = message.getTroopUpdateMessage().getCompressedTroop().getCurrentHp();

                return header + message.getTroopUpdateMessage().getCompressedTroop().getCard().getCardId()
                        + String.format(" is %d/%d and at location: ", currentAttack, currentHealth)
                        + message.getTroopUpdateMessage().getCompressedTroop().getCell();

            case CARD_POSITION:
                return header + message.getCardPositionMessage().getCard().getCardId() + " has been moved to: " + message.getCardPositionMessage().getCardPosition();

            default:
                return null;
        }
    }

    private void sendMessages() throws IOException {
        while (true) {
            Message message;
            synchronized (sendingMessages) {
                message = sendingMessages.poll();
            }
            if (message != null) {
                String json = message.toJson();
                ws.sendText(json);

                System.out.println("message sent: " + json);

            } else {
                try {
                    synchronized (sendingMessages) {
                        sendingMessages.wait();
                    }
                } catch (InterruptedException ignored) {
                }
            }
        }
    }


    private void handleMessage(Message message) {
        switch (message.getMessageType()) {
            case SEND_EXCEPTION:
                showError(message);
                break;
            case ACCOUNT_COPY:
                updateAccount(message);
                break;
            case GAME_COPY:
                GameController.getInstance().setCurrentGame(message.getGameCopyMessage().getCompressedGame());
                GameController.getInstance().calculateAvailableActions();
                break;
            case ORIGINAL_CARDS_COPY:
            case DONE:
                break;
            case CARD_POSITION://TODO:CHANGE
                CardPosition cardPosition = message.getCardPositionMessage().getCardPosition();
                switch (cardPosition) {
                    case MAP:
                        GameController.getInstance().getCurrentGame().moveCardToMap(message.getCardPositionMessage().getCard());
                        GameController.getInstance().calculateAvailableActions();
                        break;
                    case HAND:
                        GameController.getInstance().getCurrentGame().moveCardToHand();
                        GameController.getInstance().calculateAvailableActions();
                        break;
                    case NEXT:
                        GameController.getInstance().getCurrentGame().moveCardToNext(message.getCardPositionMessage().getCard());
                        GameController.getInstance().calculateAvailableActions();
                        break;
                    case GRAVE_YARD:
                        GameController.getInstance().getCurrentGame().moveCardToGraveYard(message.getCardPositionMessage().getCard());
                        GameController.getInstance().calculateAvailableActions();
                        break;
                }
                break;
            case TROOP_UPDATE:
                GameController.getInstance().getCurrentGame().troopUpdate(message.getTroopUpdateMessage().getCompressedTroop());
                GameController.getInstance().calculateAvailableActions();
                break;
            case SET_NEW_NEXT_CARD:
                GameController.getInstance().getCurrentGame().moveCardToNext( message.getCard() );
                break;
            case GAME_UPDATE:
                GameUpdateMessage gameUpdateMessage = message.getGameUpdateMessage();
                GameController.getInstance().getCurrentGame().gameUpdate(
                        gameUpdateMessage.getTurnNumber(),
                        gameUpdateMessage.getPlayer1CurrentMP(),
                        gameUpdateMessage.getPlayer2CurrentMP(),
                        gameUpdateMessage.getCellEffects());
                GameController.getInstance().calculateAvailableActions();
                break;
            case Game_FINISH:
                GameResultController.getInstance().setWinnerInfo(message.getGameFinishMessage().amIWinner(), message.getGameFinishMessage().getReward());
                if (currentShow instanceof BattleScene) {
                    ((BattleScene) currentShow).finish(message.getGameFinishMessage().amIWinner());
                }
                new Thread(() -> {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException ignored) {
                    }
                    if (((BattleScene) currentShow).getMyPlayerNumber() == -1) {
                        Platform.runLater(() -> new MainMenu().show());
                    } else
                        Platform.runLater(() -> new GameResultMenu().show());
                }).start();
                break;
            case ANIMATION:
                GameController.getInstance().showAnimation(message.getGameAnimations());
                break;
            case CHAT:
                showOrSaveMessage(message);
                break;
            case INVITATION:
                Platform.runLater(() -> currentShow.showInvite(message.getNewGameFields()));
                break;
            case ACCEPT_REQUEST:
                //think...
                break;
            case DECLINE_REQUEST:
                if (currentShow instanceof WaitingMenu) {
                    ((WaitingMenu) currentShow).close();
                }
                break;

            case ONLINE_GAMES_COPY:
                OnlineGamesListController.getInstance().setOnlineGames(message.getOnlineGames());
                break;

            case CLIENT_ID:
                clientName = message.getClientIDMessage().getID();
        }
    }

    private void showOrSaveMessage(Message message) {
        if (message.getChatMessage().getReceiverUsername() == null) {
            MainMenuController.getInstance().addChatMessage(message.getChatMessage());
        } else {
            if (currentShow instanceof BattleScene) {
                ((BattleScene) currentShow).showOpponentMessage(message.getChatMessage().getText());
            }
        }
    }

    private void showError(Message message) {
        Platform.runLater(() -> currentShow.showError(message.getExceptionMessage().getExceptionString()));
    }

    private void updateAccount(Message message) {
        if (account == null) {
            account = new Account(message.getAccountCopyMessage().getAccount());
        } else {
            account.update(message.getAccountCopyMessage().getAccount());
        }

        if (currentShow instanceof LoginMenu) {
            Platform.runLater(() -> new MainMenu().show());
        }
    }

    private void disconnected() {
    }

    public String getClientName() {
        return clientName;
    }

    public Account getAccount() {
        return account;
    }

    void close() {
        ws.disconnect();
        MainMenuController.getInstance().logout();
        System.exit(0);
    }

    public synchronized Show getCurrentShow() {
        if (currentShow == null) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return currentShow;
    }

    public synchronized void setShow(Show show) {
        this.currentShow = show;
        notifyAll();
    }

    public void makeConnection() {
        new Thread(() -> {
            try {
                connect();
            } catch (IOException e) {
                e.printStackTrace();
                getCurrentShow();
                Platform.runLater(() ->
                        getCurrentShow().showError("Connection failed", "RETRY", event -> makeConnection())
                );
                disconnected();
            }
        }).start();
    }


}
