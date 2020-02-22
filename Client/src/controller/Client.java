package controller;

import com.google.gson.Gson;
import javafx.application.Platform;
import models.Constants;
import models.account.Account;
import server.dataCenter.models.card.Card;
import server.dataCenter.models.card.DeckInfo;
import models.message.CardPosition;
import models.message.GameUpdateMessage;
import models.message.Message;
import server.dataCenter.DataCenter;
import view.BattleView.BattleScene;
import view.*;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.LinkedList;

public class Client {
    private static final String CONFIG_PATH = "config";
    private static Client client;
    private final LinkedList<Message> sendingMessages = new LinkedList<>();
    private String clientName;
    private Account account;
    private Show currentShow;
    private Socket socket;
    private final Gson gson = new Gson();
    private Thread sendMessageThread;
    private BufferedReader bufferedReader;

    private Client() {
    }

    public static Client getInstance() {
        if (client == null) {
            client = new Client();
        }
        return client;
    }

    private void connect() throws IOException, NullPointerException {
        socket = getSocketReady();
        sendClientNameToServer(socket);
        sendMessageThread = new Thread(() -> {
            try {
                sendMessages();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        sendMessageThread.start();
        receiveMessages();
    }

    private void sendClientNameToServer(Socket socket) throws IOException {
        while (!bufferedReader.readLine().equals("#Listening#")) ;
        System.out.println("server is listening to me");

        clientName = InetAddress.getLocalHost().getHostName();
        socket.getOutputStream().write(("#" + clientName + "#\n").getBytes());
        int x = 1;
        String finalClientName = clientName;
        while (!bufferedReader.readLine().equals("#Valid#")) {
            x++;
            finalClientName = clientName + x;
            socket.getOutputStream().write(("#" + finalClientName + "#\n").getBytes());
        }
        clientName = finalClientName;
        System.out.println("server accepted me.");
    }

    private Socket getSocketReady() throws IOException {
        Socket socket = makeSocket();
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        System.out.println("network connected.");

        return socket;
    }

    private Socket makeSocket() throws IOException {
        File file = new File(CONFIG_PATH);
        if (file.exists()) {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            try {
                String ip = reader.readLine();
                int port = Integer.parseInt(reader.readLine());
                return new Socket(ip, port);
            } catch (NumberFormatException e) {
                System.out.println("wrong format for port in config");
                System.exit(0);
            }
        }
        return new Socket(Constants.SERVER_IP, Constants.PORT);
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
                return header + message.getCardPositionMessage().getCompressedCard().getCardId() + " has been moved to: " + message.getCardPositionMessage().getCardPosition();

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
                socket.getOutputStream().write((json + "\n").getBytes());

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

    private void receiveMessages() throws IOException {
        while (true) {
            String json = bufferedReader.readLine();
            Message message = gson.fromJson(json, Message.class);

            String msg = simplifyLogMessage(message, "Server");
            if (msg != null) {
                System.out.println(msg);
            } else {
                System.out.println(json);
            }
            handleMessage(message);
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
                        GameController.getInstance().getCurrentGame().moveCardToMap(message.getCardPositionMessage().getCompressedCard());
                        GameController.getInstance().calculateAvailableActions();
                        break;
                    case HAND:
                        GameController.getInstance().getCurrentGame().moveCardToHand();
                        GameController.getInstance().calculateAvailableActions();
                        break;
                    case NEXT:
                        GameController.getInstance().getCurrentGame().moveCardToNext(message.getCardPositionMessage().getCompressedCard());
                        GameController.getInstance().calculateAvailableActions();
                        break;
                    case GRAVE_YARD:
                        GameController.getInstance().getCurrentGame().moveCardToGraveYard(message.getCardPositionMessage().getCompressedCard());
                        GameController.getInstance().calculateAvailableActions();
                        break;
                }
                break;
            case TROOP_UPDATE:
                GameController.getInstance().getCurrentGame().troopUpdate(message.getTroopUpdateMessage().getCompressedTroop());
                GameController.getInstance().calculateAvailableActions();
                break;
            case SET_NEW_NEXT_CARD:
                GameController.getInstance().getCurrentGame().moveCardToNext( message.getCompressedCard() );
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
        try {
            if (socket != null) {
                socket.close();
                System.out.println("socket closed");
            }
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
