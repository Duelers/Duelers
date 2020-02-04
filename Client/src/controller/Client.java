package controller;

import com.google.gson.Gson;
import javafx.application.Platform;
import models.Constants;
import models.account.Account;
import models.account.AccountInfo;
import models.card.Card;
import models.card.DeckInfo;
import models.comperessedData.CompressedCard;
import models.game.map.Position;
import models.message.CardPosition;
import models.message.GameUpdateMessage;
import models.message.Message;
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
    private LinkedList<Message> receivingMessages = new LinkedList<>();
    private DeckInfo[] customDecks;
    private AccountInfo[] leaderBoard;
    private Card selected;
    private Position[] positions;
    private boolean validation = true;
    private String errorMessage;
    private Socket socket;
    private Gson gson = new Gson();
    private Thread sendMessageThread;
    private Thread receiveMessageThread;
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

    private void sendMessages() throws IOException {
        System.out.println("sending messages started");
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
        System.out.println("receiving messages started.");
        while (true) {
            String json = bufferedReader.readLine();
            Message message = gson.fromJson(json, Message.class);

            System.out.println("message received: " + json);

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
                ShopController.getInstance().setOriginalCards(message.getCardsCopyMessage().getCards());
                break;
            case LEADERBOARD_COPY:
                MainMenuController.getInstance().setLeaderBoard(message.getLeaderBoardCopyMessage().getLeaderBoard());
                break;
            case STORIES_COPY:
                StoryMenuController.getInstance().setStories(message.getStoriesCopyMessage().getStories());
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
                    case COLLECTED:
                        GameController.getInstance().getCurrentGame().moveCardToCollectedItems(message.getCardPositionMessage().getCompressedCard());
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
                        gameUpdateMessage.getPlayer1NumberOfCollectedFlags(),
                        gameUpdateMessage.getPlayer2CurrentMP(),
                        gameUpdateMessage.getPlayer2NumberOfCollectedFlags(),
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
            case DONE:
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
            case CHANGE_CARD_NUMBER:
                ShopAdminController.getInstance().setValue(
                        message.getChangeCardNumber().getCardName(),
                        message.getChangeCardNumber().getNumber()
                );
                break;
            case ADD_TO_ORIGINALS:
                if (ShopController.isLoaded()) {
                    ShopController.getInstance().addCard(message.getCard());
                }
                break;
            case ADD_TO_CUSTOM_CARDS:
                CustomCardRequestsController.getInstance().addCard(message.getCard());
                break;
            case REMOVE_FROM_CUSTOM_CARDS:
                CustomCardRequestsController.getInstance().removeCard(message.getCardName());
                break;
            case CUSTOM_CARDS_COPY:
                CustomCardRequestsController.getInstance().setCustomCardRequests(message.getCardsCopyMessage().getCards());
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
        validation = false;
        errorMessage = message.getExceptionMessage().getExceptionString();
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
