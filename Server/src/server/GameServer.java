package server;

import server.chatCenter.ChatCenter;
import server.clientPortal.ClientPortal;
import server.dataCenter.models.card.ServerCard;
import server.services.RemoteTokenVerificationService;
import server.clientPortal.models.message.CardPosition;
import server.clientPortal.models.message.Message;
import server.clientPortal.models.message.OnlineGame;
import server.dataCenter.DataCenter;
import server.dataCenter.models.account.Account;
import server.dataCenter.models.account.AccountType;
import shared.models.card.spell.AvailabilityType;
import server.exceptions.ClientException;
import server.exceptions.LogicException;
import server.exceptions.ServerException;
import server.gameCenter.GameCenter;
import server.gameCenter.models.game.*;
import shared.models.game.map.Cell;
import shared.models.game.map.CellEffect;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class GameServer {
    private static GameServer server;
    public static String serverName;

    private final static Queue<Message> receivingMessages = new LinkedList<>();

    private GameServer(String serverName) {
        GameServer.serverName = serverName;
        serverPrint("Server Was Created!");
    }

    public static GameServer getInstance() {
        return server;
    }

    public static void start() {
        server = new GameServer("Server");
        DataCenter.getInstance().start();//no thread
        GameCenter.getInstance().start();
        ClientPortal.getInstance().start();

        new Thread(() -> {
            serverPrint("Server Thread:receiving messages is started...");
            while (true) {
                Message message;
                synchronized (receivingMessages) {
                    message = receivingMessages.poll();
                }
                if (message != null) {
                    receiveMessage(message);
                } else {
                    try {
                        synchronized (receivingMessages) {
                            receivingMessages.wait();
                        }
                    } catch (InterruptedException ignored) {
                    }
                }
            }
        }).start();
    }

    public static void sendMessageAsync(Message message) {
        ClientPortal.getInstance().prepareAndSendMessageAsync(message.getReceiver(), message);
    }

    public static void addToReceivingMessages(Message message) {
        synchronized (receivingMessages) {
            receivingMessages.add(message);
            receivingMessages.notify();
        }
    }

    private static void receiveMessage(Message message) {
        try {
            if (message == null) {
                throw new ServerException("NULL Message");
            }
            if (!message.getReceiver().equals(serverName)) {
                throw new ServerException("Message's Receiver Was Not This Server.");
            }
            switch (message.getMessageType()) {
                case AUTHENTICATE:
                    RemoteTokenVerificationService.getInstance().verifyAuthenticationToken(message.token)
                            .thenAccept(r -> {
                                if (r.error == null) {
                                    DataCenter.getInstance().loginOrRegister(r.username, message.getSender());
                                } else {
                                    serverPrint(r.error);
                                }
                            });
                    break;
                case LOG_OUT:
                    DataCenter.getInstance().logout(message);
                    break;
                case GET_DATA:
                    switch (message.getGetDataMessage().getDataName()) {
                        case ORIGINAL_CARDS:
                            sendOriginalCards(message);
                            break;

                        case ONLINE_GAMES_LIST:
                            sendOnlineGames(message);
                            break;
                    }
                    break;
                case BUY_CARD:
                    DataCenter.getInstance().buyCard(message);
                    break;
                case CREATE_DECK:
                    DataCenter.getInstance().createDeck(message);
                    break;
                case REMOVE_DECK:
                    DataCenter.getInstance().removeDeck(message);
                    break;
                case IMPORT_DECK:
                    DataCenter.getInstance().importDeck(message);
                    break;
                case ADD_TO_DECK:
                    DataCenter.getInstance().addToDeck(message);
                    break;
                case REMOVE_FROM_DECK:
                    DataCenter.getInstance().removeFromDeck(message);
                    break;
                case SELECT_DECK:
                    DataCenter.getInstance().selectDeck(message);
                    break;
                case MULTIPLAYER_GAME_REQUEST:
                    GameCenter.getInstance().getMultiPlayerGameRequest(message);
                    sendMessageAsync(Message.makeDoneMessage(message.getSender()));
                    break;
                case CANCEL_REQUEST:
                    GameCenter.getInstance().getCancelRequest(message);
                    sendMessageAsync(Message.makeDoneMessage(message.getSender()));
                    break;
                case ACCEPT_REQUEST:
                    GameCenter.getInstance().getAcceptRequest(message);
                    sendMessageAsync(Message.makeDoneMessage(message.getSender()));
                    break;
                case DECLINE_REQUEST:
                    GameCenter.getInstance().getDeclineRequest(message);
                    sendMessageAsync(Message.makeDoneMessage(message.getSender()));
                    break;
                case NEW_DECK_GAME:
                    GameCenter.getInstance().newDeckGame(message);
                    sendMessageAsync(Message.makeDoneMessage(message.getSender()));
                    break;
                case INSERT:
                    GameCenter.getInstance().insertCard(message);
                    sendMessageAsync(Message.makeDoneMessage(message.getSender()));
                    break;
                case ATTACK:
                    GameCenter.getInstance().attack(message);
                    sendMessageAsync(Message.makeDoneMessage(message.getSender()));
                    break;
                case END_TURN:
                    GameCenter.getInstance().endTurn(message);
                    sendMessageAsync(Message.makeDoneMessage(message.getSender()));
                    break;
                case MOVE_TROOP:
                    GameCenter.getInstance().moveTroop(message);
                    sendMessageAsync(Message.makeDoneMessage(message.getSender()));
                    break;
                case SET_NEW_NEXT_CARD:
                    GameCenter.getInstance().setNewNextCard(message);
                    sendMessageAsync(Message.makeDoneMessage(message.getSender()));
                    break;
                case REPLACE_CARD:
                    GameCenter.getInstance().replaceCard(message);
                    break;
                case FORCE_FINISH:
                    GameCenter.getInstance().forceFinishGame(message.getSender());
                    break;
                case SELECT_USER:
                    selectUserForMultiPlayer(message);
                    break;
                case CHAT:
                    ChatCenter.getInstance().getMessage(message);
                    break;
                case SUDO:
                    sudo(message);
                    break;
                case CHANGE_CARD_NUMBER:
                    DataCenter.getInstance().changeCardNumber(message);
                    break;
                case CHANGE_ACCOUNT_TYPE:
                    DataCenter.getInstance().changeAccountType(message);
                    break;
                case ONLINE_GAME_SHOW_REQUEST:
                    GameCenter.getInstance().addOnlineShowRequest(message);
                    break;
                case STOP_SHOW_GAME:
                    GameCenter.getInstance().removeOnlineShowGame(message);
                    break;
                default:
                    throw new LogicException("Invalid Message Type!");
            }
        } catch (ServerException e) {
            serverPrint(e.getMessage());
            if (message != null) {
                sendException("server has error:(", message.getSender());
            }
        } catch (LogicException e) {
            serverPrint(e.getMessage());
            sendException(e.getMessage(), message.getSender());
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendException(String exceptionString, String receiver) {
        sendMessageAsync(Message.makeExceptionMessage(receiver, exceptionString));
    }

    private static void sendOnlineGames(Message message) throws LogicException {
        DataCenter.getInstance().loginCheck(message);
        Account account = DataCenter.getInstance().getClients().get(message.getSender());
        OnlineGame[] onlines = GameCenter.getInstance().getOnlineGames();
        sendMessageAsync(Message.makeOnlineGamesCopyMessage(message.getSender(), onlines));
    }

    private static void sendOriginalCards(Message message) throws LogicException {
        DataCenter.getInstance().loginCheck(message);
        sendMessageAsync(Message.makeOriginalCardsCopyMessage(message.getSender(), DataCenter.getInstance().getOriginalCards()));
    }


    private static void selectUserForMultiPlayer(Message message) throws ClientException {
        Account account = DataCenter.getInstance().getAccount(message.getNewGameFields().getOpponentUsername());
        if (account == null) {
            throw new ClientException("second player is not valid");
        } else if (!account.hasValidMainDeck()) {
            throw new ClientException("selected deck for second player is not valid");
        } else {
            sendMessageAsync(Message.makeAccountInfoMessage(message.getSender(), account));
        }
    }

    private static void sudo(Message message) {
        String command = message.getOtherFields().getSudoCommand().toLowerCase();
        if (command.contains("account")) {
            for (Account account : DataCenter.getInstance().getAccounts().keySet()) {
                serverPrint(account.getUsername() + " " + account.getPassword());
            }
        }
        sendMessageAsync(Message.makeDoneMessage(message.getSender()));
    }

    public void sendChangeCardPositionMessage(Game game, ServerCard card, CardPosition newCardPosition) {
        for (Account account : game.getObservers()) {
            String clientName = DataCenter.getInstance().getAccounts().get(account);
            if (clientName == null) {
                serverPrint("*Error");
                continue;
            }
            sendMessageAsync(Message.makeChangeCardPositionMessage(clientName, card, newCardPosition));
        }
    }

    public void sendNewNextCardSetMessage(Game game, ServerCard nextCard) {
        for (Account account : game.getObservers()) {
            String clientName = DataCenter.getInstance().getAccounts().get(account);
            if (clientName == null) {
                serverPrint("*Error: Client not found");
                continue;
            }
            sendMessageAsync(Message.makeNewNextCardSetMessage(clientName, nextCard));
        }
    }

    public void sendTroopUpdateMessage(Game game, ServerTroop troop) {
        for (Account account : game.getObservers()) {
            String clientName = DataCenter.getInstance().getAccounts().get(account);
            if (clientName == null) {
                serverPrint("*Error");
                continue;
            }
            sendMessageAsync(Message.makeTroopUpdateMessage(clientName, troop));
        }
    }

    public void sendAttackMessage(Game game, ServerTroop attacker, ServerTroop defender, boolean counterAttack) {
        for (Account account : game.getObservers()) {
            String clientName = DataCenter.getInstance().getAccounts().get(account);
            if (clientName == null) {
                serverPrint("*Error");
                continue;
            }
            sendMessageAsync(Message.makeAttackMessage(clientName, attacker, defender, counterAttack));
        }
    }

    public void sendSpellMessage(Game game, TargetData target, AvailabilityType availabilityType) {
        Set<Cell> cells = target.getPositions();
        if (cells.size() == 0) return;
        for (Account account : game.getObservers()) {
            String clientName = DataCenter.getInstance().getAccounts().get(account);
            if (clientName == null) {
                serverPrint("*Error");
                continue;
            }
            sendMessageAsync(Message.makeSpellMessage(clientName, cells, availabilityType));
        }
    }

    public void sendGameUpdateMessage(Game game) {
        List<CellEffect> cellEffects = game.getCellEffects();
        for (Account account : game.getObservers()) {
            String clientName = DataCenter.getInstance().getAccounts().get(account);
            if (clientName == null) {
                serverPrint("*Error");
                continue;
            }
            sendMessageAsync(Message.makeGameUpdateMessage(
                    clientName, game.getTurnNumber(), game.getPlayerOne().getCurrentMP(), game.getPlayerTwo().getCurrentMP(), cellEffects));
        }
    }

    public void sendGameFinishMessages(Game game) {
        for (Account account : game.getObservers()) {
            String clientName = DataCenter.getInstance().getAccounts().get(account);
            if (clientName == null) {
                serverPrint("*Error");
                continue;
            }
            if (account.getUsername().equals(game.getPlayerOne().getUserName())) {
                sendMessageAsync(Message.makeGameFinishMessage(
                        clientName, game.getPlayerOne().getMatchHistory().isAmIWinner()));
                sendMessageAsync(Message.makeAccountCopyMessage(
                        clientName, DataCenter.getInstance().getAccount(game.getPlayerOne().getUserName())));
            } else if (account.getUsername().equals(game.getPlayerTwo().getUserName())) {
                sendMessageAsync(Message.makeGameFinishMessage(
                        clientName, game.getPlayerTwo().getMatchHistory().isAmIWinner()));
                sendMessageAsync(Message.makeAccountCopyMessage(
                        clientName, DataCenter.getInstance().getAccount(game.getPlayerTwo().getUserName())));
            } else {
                sendMessageAsync(Message.makeGameFinishMessage(
                        clientName, false));
            }
        }
    }

    public static void serverPrint(String string) {
        System.out.println("\u001B[32m" + string.trim() + "\u001B[0m");
    }

    public void sendChangeCardNumberMessage(ServerCard card) {
        for (Account account : DataCenter.getInstance().getAccounts().keySet()) {
            if (account.getAccountType().equals(AccountType.ADMIN) && DataCenter.getInstance().isOnline(account.getUsername())) {
                sendMessageAsync(Message.makeChangeCardNumberMessage(DataCenter.getInstance().getAccounts().get(account),
                        card, card.getRemainingNumber()));
            }
        }
    }

    public void sendAddToOriginalsMessage(ServerCard card) {
        for (Account account : DataCenter.getInstance().getAccounts().keySet()) {
            if (DataCenter.getInstance().isOnline(account.getUsername())) {
                sendMessageAsync(
                        Message.makeAddOriginalCardMessage(DataCenter.getInstance().getAccounts().get(account), card)
                );
            }
        }
    }

    public void sendAccountUpdateMessage(Account account) {
        String clientName = DataCenter.getInstance().getAccounts().get(account);
        if (clientName == null)
            return;
        sendMessageAsync(Message.makeAccountCopyMessage(clientName, account));
    }

    public void sendCardsDrawnToHandMessage(Game game, int deckSize, ServerCard... drawnCards) {
        for (Account account : game.getObservers()) {
            String clientName = DataCenter.getInstance().getAccounts().get(account);
            if (clientName == null) {
                serverPrint("*Error: Failed to get client name of account " + account.getUsername() + ". Deleting from list of observers.");
                game.getObservers().remove(account);
                continue;
            }
            sendMessageAsync(Message.makeCardsDrawnFromDeckMessage(clientName, deckSize, drawnCards));
        }
    }
}