package controller;

import Config.Config;
import com.google.gson.Gson;
import com.neovisionaries.ws.client.*;
import javafx.application.Platform;
import models.account.Account;
import models.message.CardPosition;
import models.message.GameUpdateMessage;
import models.message.Message;
import view.BattleView.BattleScene;
import view.*;

import java.io.*;
import java.util.LinkedList;


public class Client {
    private WebSocket ws;
    private static Client client;
    private final LinkedList<Message> sendingMessages = new LinkedList<>();
    private String clientName;
    private Account account;
    private Show currentShow;
    private final Gson gson = new Gson();
    private static final String serverName = Config.getInstance().getProperty("SERVER_NAME");


    public static Client getInstance() {
        if (client == null) {
            client = new Client();
        }
        return client;
    }

    private void connect() throws IOException, NullPointerException {
        String serverUri = Config.getInstance().getProperty("SERVER_URI");
        if (serverUri == null) {
            String serverIp = Config.getInstance().getProperty("SERVER_IP");
            String port = Config.getInstance().getProperty("PORT");
            serverUri = "ws://" + serverIp + ":" + port;
        }
        int connectionAttempts = 5;

        Thread sendMessageThread = new Thread(() -> {
            try {
                sendMessages();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        this.ws = new WebSocketFactory().createSocket(serverUri + "/websockets/game");
        this.ws.addListener(new WebSocketAdapter() {
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
        while (true) {
            try {
                this.ws.connect();
                break;
            } catch (WebSocketException e) {
                System.out.println(e.getMessage());
                connectionAttempts -= 1;
                if (connectionAttempts == 0) {
                    throw new RuntimeException(e);
                }
                this.ws = this.ws.recreate();
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
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

                int currentAttack = message.getTroopUpdateMessage().getTroop().getCurrentAp();
                int currentHealth = message.getTroopUpdateMessage().getTroop().getCurrentHp();

                return header + message.getTroopUpdateMessage().getTroop().getCard().getCardId()
                        + String.format(" is %d/%d and at location: ", currentAttack, currentHealth)
                        + message.getTroopUpdateMessage().getTroop().getCell();

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
                this.ws.sendText(json);

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
                GameController.getInstance().getCurrentGame().troopUpdate(message.getTroopUpdateMessage().getTroop());
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
                GameResultController.getInstance().setWinnerInfo(message.getGameFinishMessage().amIWinner());
                if (this.currentShow instanceof BattleScene) {
                    ((BattleScene) this.currentShow).finish(message.getGameFinishMessage().amIWinner());
                }
                new Thread(() -> {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException ignored) {
                    }
                    if (((BattleScene) this.currentShow).getMyPlayerNumber() == -1) {
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
                Platform.runLater(() -> this.currentShow.showInvite(message.getNewGameFields()));
                break;
            case ACCEPT_REQUEST:
                //think...
                break;
            case DECLINE_REQUEST:
                if (this.currentShow instanceof WaitingMenu) {
                    ((WaitingMenu) this.currentShow).close();
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
            if (this.currentShow instanceof BattleScene) {
                ((BattleScene) this.currentShow).showOpponentMessage(message.getChatMessage().getText());
            }
        }
    }

    private void showError(Message message) {
        Platform.runLater(() -> this.currentShow.showError(message.getExceptionMessage().getExceptionString()));
    }

    private void updateAccount(Message message) {
        if (this.account == null) {
            this.account = new Account(message.getAccountCopyMessage().getAccount());
        } else {
            this.account.update(message.getAccountCopyMessage().getAccount());
        }

        if (this.currentShow instanceof LoginMenu) {
            Platform.runLater(() -> new MainMenu().show());
        }
    }

    private void disconnected() {
    }

    public String getClientName() {
        return this.clientName;
    }

    public Account getAccount() {
        return this.account;
    }

    public void close() {
        Message message = Message.makeLogOutMessage(serverName);
        String json = message.toJson();
        this.ws.sendText(json);
        this.ws.disconnect();
        System.exit(0);
    }

    public synchronized Show getCurrentShow() {
        if (this.currentShow == null) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return this.currentShow;
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
