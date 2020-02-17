package server.gameCenter;

import server.Server;
import server.clientPortal.models.message.Message;
import server.clientPortal.models.message.OnlineGame;
import server.dataCenter.DataCenter;
import server.dataCenter.models.account.Account;
import server.dataCenter.models.account.AccountType;
import server.dataCenter.models.account.MatchHistory;
import server.dataCenter.models.card.Deck;
import server.exceptions.ClientException;
import server.exceptions.LogicException;
import server.gameCenter.models.GlobalRequest;
import server.gameCenter.models.UserInvitation;
import server.gameCenter.models.game.*;
import server.gameCenter.models.map.GameMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class GameCenter extends Thread {//synchronize
    private static GameCenter ourInstance = new GameCenter();
    private final HashMap<Account, Game> onlineGames = new HashMap<>();//Account -> Game
    private final LinkedList<GlobalRequest> globalRequests = new LinkedList<>();
    private final LinkedList<UserInvitation> userInvitations = new LinkedList<>();
    private final ArrayList<OnlineGame> gameInfos = new ArrayList<>();

    private GameCenter() {
    }

    public static GameCenter getInstance() {
        return ourInstance;
    }

    @Override
    public void run() {
        Server.getInstance().serverPrint("Starting GameCenter...");
    }

    private Game getGame(String clientName) throws ClientException {
        Account account = DataCenter.getInstance().getClients().get(clientName);
        if (account == null) {
            throw new ClientException("your client hasn't logged in!");
        }
        Game game = onlineGames.get(account);
        if (game == null) {
            throw new ClientException("you don't have online game!");
        }
        return game;
    }

    private Game getGame(OnlineGame onlineGame) {
        if (onlineGame.getPlayer1() != null) {
            Account account = DataCenter.getInstance().getAccount(onlineGame.getPlayer1());
            if (account != null)
                return onlineGames.get(account);
        }
        if (onlineGame.getPlayer2() != null) {
            Account account = DataCenter.getInstance().getAccount(onlineGame.getPlayer2());
            if (account != null)
                return onlineGames.get(account);
        }
        return null;
    }

    public void getMultiPlayerGameRequest(Message message) throws LogicException {
        DataCenter.getInstance().loginCheck(message.getSender());
        Account account1 = DataCenter.getInstance().getClients().get(message.getSender());
        if (onlineGames.get(account1) != null)
            throw new ClientException("You have online game!");
        if (!account1.hasValidMainDeck())
            throw new ClientException("You don't have valid main deck");
        if (message.getNewGameFields() == null || message.getNewGameFields().getGameType() == null)
            throw new ClientException("Invalid Request");
        if (message.getNewGameFields().getOpponentUsername() == null) {
            addGlobalRequest(account1, message.getNewGameFields().getGameType());
        } else {
            Account account2 = DataCenter.getInstance().getAccount(message.getNewGameFields().getOpponentUsername());
            checkOpponentAccountValidation(account2);
            addUserInvitation(account1, account2, message.getNewGameFields().getGameType());
        }
    }

    private void addGlobalRequest(Account account, GameType gameType) {
        removeAllGameRequests(account);
        synchronized (globalRequests) {
            for (GlobalRequest globalRequest : globalRequests) {
                if (globalRequest.getGameType() == gameType) {
                    newMultiplayerGame(globalRequest.getRequester(), account, gameType);
                    globalRequests.remove(globalRequest);
                    return;
                }
            }
            globalRequests.addLast(new GlobalRequest(account, gameType));
        }
    }

    private void addUserInvitation(Account inviter, Account invited, GameType gameType) {
        removeAllGameRequests(inviter);
        synchronized (userInvitations) {
            userInvitations.addLast(new UserInvitation(inviter, invited, gameType));
            Server.getInstance().addToSendingMessages(Message.makeInvitationMessage(
                    DataCenter.getInstance().getAccounts().get(invited), inviter.getUsername(),
                    gameType));
        }
    }

    public void removeAllGameRequests(Account account) {
        synchronized (globalRequests) {
            for (GlobalRequest globalRequest : globalRequests) {
                if (globalRequest.getRequester() == account) {
                    globalRequests.remove(globalRequest);
                    break;
                }
            }
        }
        synchronized (userInvitations) {
            for (UserInvitation userInvitation : userInvitations) {
                if (userInvitation.getInviter() == account) {
                    userInvitations.remove(userInvitation);
                    break;
                }
            }
        }
    }

    public OnlineGame[] getOnlineGames() {
        return gameInfos.toArray(new OnlineGame[]{});
    }

    private UserInvitation getUserInvitation(Account inviter) {
        for (UserInvitation userInvitation : userInvitations) {
            if (userInvitation.getInviter() == inviter)
                return userInvitation;
        }
        return null;
    }

    public void getAcceptRequest(Message message) throws LogicException {
        DataCenter.getInstance().loginCheck(message.getSender());
        Account invited = DataCenter.getInstance().getClients().get(message.getSender());
        synchronized (userInvitations) {
            if (message.getNewGameFields() == null || message.getNewGameFields().getOpponentUsername() == null)
                throw new ClientException("invalid accept message!");
            Account inviter = DataCenter.getInstance().getAccount(message.getNewGameFields().getOpponentUsername());
            if (inviter == null)
                throw new ClientException("invalid opponent username!");
            UserInvitation invitation = getUserInvitation(inviter);
            if (invitation == null)
                throw new ClientException("The Invitation was not found!");
            userInvitations.remove(invitation);
            Server.getInstance().addToSendingMessages(Message.makeAcceptRequestMessage(
                    DataCenter.getInstance().getAccounts().get(inviter)));
            newMultiplayerGame(inviter, invited, invitation.getGameType());
        }
    }

    public void getDeclineRequest(Message message) throws LogicException {
        DataCenter.getInstance().loginCheck(message.getSender());
        Account account = DataCenter.getInstance().getClients().get(message.getSender());
        synchronized (userInvitations) {
            if (message.getNewGameFields() == null || message.getNewGameFields().getOpponentUsername() == null)
                throw new ClientException("invalid accept message!");
            Account inviter = DataCenter.getInstance().getAccount(message.getNewGameFields().getOpponentUsername());
            if (inviter == null)
                throw new ClientException("invalid opponent username!");
            UserInvitation invitation = getUserInvitation(inviter);
            if (invitation == null)
                throw new ClientException("The Invitation was not found!");
            userInvitations.remove(invitation);
            Server.getInstance().addToSendingMessages(Message.makeDeclineRequestMessage(
                    DataCenter.getInstance().getAccounts().get(inviter)));
        }
    }

    public void getCancelRequest(Message message) throws LogicException {
        DataCenter.getInstance().loginCheck(message.getSender());
        Account account = DataCenter.getInstance().getClients().get(message.getSender());
        removeAllGameRequests(account);
    }

    private void checkOpponentAccountValidation(Account opponentAccount) throws LogicException {
        if (opponentAccount == null) {
            throw new ClientException("invalid opponent username!");
        }
        if (DataCenter.getInstance().getAccounts().get(opponentAccount) == null) {
            throw new ClientException("opponentAccount is not online!");
        }
        if (!opponentAccount.hasValidMainDeck()) {
            throw new ClientException("opponent doesn't have valid main deck");
        }
        if (onlineGames.get(opponentAccount) != null) {
            throw new ClientException("opponentAccount has online game!");
        }
        //TODO:Validation
    }

    public void newDeckGame(Message message) throws LogicException {
        DataCenter.getInstance().loginCheck(message);
        Account myAccount = DataCenter.getInstance().getClients().get(message.getSender());
        removeAllGameRequests(myAccount);
        if (!myAccount.hasValidMainDeck()) {
            throw new ClientException("you don't have valid main deck!");
        }
        if (onlineGames.get(myAccount) != null) {
            throw new ClientException("you have online game!");
        }
        Deck deck = new Deck(myAccount.getDeck(message.getNewGameFields().getCustomDeckName()));
        if (deck == null || !deck.isValid()) {
            throw new ClientException("selected deck is not valid");
        }
        deck.makeCustomGameDeck();
        Game game = null;
        GameMap gameMap = new GameMap();
        switch (message.getNewGameFields().getGameType()) {
            case KILL_HERO:
                game = new KillHeroBattle(myAccount, deck, gameMap);
                game.addObserver(myAccount);
                break;
        }
        game.setReward(Game.getDefaultReward());
        onlineGames.put(myAccount, game);
        gameInfos.add(new OnlineGame(game));
        Server.getInstance().addToSendingMessages(Message.makeGameCopyMessage
                (message.getSender(), game));
        game.startGame();
    }


    private void newMultiplayerGame(Account account1, Account account2, GameType gameType) {

        removeAllGameRequests(account1);
        removeAllGameRequests(account2);
        Game game = null;
        GameMap gameMap = new GameMap();
        switch (gameType) {
            case KILL_HERO:
                game = new KillHeroBattle(account1, account2, gameMap);
                game.addObserver(account1);
                game.addObserver(account2);
                break;

        }
        game.setReward(Game.getDefaultReward());
        onlineGames.put(account1, game);
        onlineGames.put(account2, game);
        gameInfos.add(new OnlineGame(game));
        Server.getInstance().addToSendingMessages(Message.makeGameCopyMessage
                (DataCenter.getInstance().getClientName(account1.getUsername()), game));
        Server.getInstance().addToSendingMessages(Message.makeGameCopyMessage
                (DataCenter.getInstance().getClientName(account2.getUsername()), game));
        game.startGame();
    }

    private void removeGame(Game game) {
        if (!game.getPlayerOne().getUserName().equalsIgnoreCase("AI")) {
            Account account1 = DataCenter.getInstance().getAccount(game.getPlayerOne().getUserName());
            if (account1 != null) {
                onlineGames.remove(account1);
            }
        }
        if (!game.getPlayerTwo().getUserName().equalsIgnoreCase("AI")) {
            Account account2 = DataCenter.getInstance().getAccount(game.getPlayerTwo().getUserName());
            if (account2 != null) {
                onlineGames.remove(account2);
            }
        }

        for (OnlineGame onlineGame : gameInfos) {
            if (onlineGame.getPlayer1().equals(game.getPlayerOne().getUserName()) ||
                    onlineGame.getPlayer2().equals(game.getPlayerTwo().getUserName())) {
                gameInfos.remove(onlineGame);
                return;
            }
        }

    }

    public void insertCard(Message message) throws LogicException {
        Game game = getGame(message.getSender());
        game.insert(
                DataCenter.getInstance().getClients().get(message.getSender()).getUsername(),
                message.getOtherFields().getMyCardId(), message.getOtherFields().getPosition()
        );
        Server.getInstance().sendGameUpdateMessage(game);
    }

    public void attack(Message message) throws LogicException {
        Game game = getGame(message.getSender());
        game.attack(
                DataCenter.getInstance().getClients().get(message.getSender()).getUsername(),
                message.getOtherFields().getMyCardId(), message.getOtherFields().getOpponentCardId()
        );
    }

    public void moveTroop(Message message) throws LogicException {
        Game game = getGame(message.getSender());
        game.moveTroop(
                DataCenter.getInstance().getClients().get(message.getSender()).getUsername(),
                message.getOtherFields().getMyCardId(), message.getOtherFields().getPosition()
        );
    }

    public void setNewNextCard(Message message) throws LogicException{
        DataCenter.getInstance().loginCheck(message);
        Game game = getGame(message.getSender());
        game.setNewNextCard();
    }

    public void replaceCard(Message message) throws LogicException {
        DataCenter.getInstance().loginCheck(message);
        Game game = getGame(message.getSender());
        game.replaceCard(message.getCardID());
    }

    public void endTurn(Message message) throws LogicException {
        Game game = getGame(message.getSender());
        game.changeTurn(DataCenter.getInstance().getClients().get(message.getSender()).getUsername());
    }

    public void checkGameFinish(Game game) {
        if (game.finishCheck()) {
            finish(game);
        }
    }

    private void finish(Game game) {
        MatchHistory playerOneHistory = game.getPlayerOne().getMatchHistory();
        MatchHistory playerTwoHistory = game.getPlayerTwo().getMatchHistory();
        if (!game.getPlayerOne().getUserName().equalsIgnoreCase("AI")) {
            Account account = DataCenter.getInstance().getAccount(game.getPlayerOne().getUserName());
            if (account == null)
                Server.getInstance().serverPrint("Error");
            else {
                account.addMatchHistory(playerOneHistory, game.getReward());
                DataCenter.getInstance().saveAccount(account);
            }
        }
        if (!game.getPlayerTwo().getUserName().equalsIgnoreCase("AI")) {
            Account account = DataCenter.getInstance().getAccount(game.getPlayerTwo().getUserName());
            if (account == null)
                Server.getInstance().serverPrint("Error");
            else {
                account.addMatchHistory(playerTwoHistory, game.getReward());
                DataCenter.getInstance().saveAccount(account);
            }
        }
        Server.getInstance().sendGameFinishMessages(game);
        removeGame(game);
    }

    public void forceFinishGame(String sender) {
        try {
            Game game = getGame(sender);

            if (game == null) {
                Server.getInstance().serverPrint("Error forceGameFinish!");
                return;
            }
            DataCenter.getInstance().loginCheck(sender);
            String username = DataCenter.getInstance().getClients().get(sender).getUsername();

            game.forceFinish(username);
            finish(game);
        } catch (LogicException ignored) {
        }

    }

    public void addOnlineShowRequest(Message message) throws LogicException {
        DataCenter.getInstance().loginCheck(message);
        Account account = DataCenter.getInstance().getClients().get(message.getSender());
        if (account.getAccountType() != AccountType.ADMIN)
            throw new ClientException("You don't have admin access!");
        Game game = getGame(message.getOnlineGame());
        if (game == null)
            throw new ClientException("Invalid Game");
        Server.getInstance().addToSendingMessages(Message.makeGameCopyMessage(message.getSender(), game));
        game.addObserver(account);
    }

    public void removeOnlineShowGame(Message message) throws LogicException {
        DataCenter.getInstance().loginCheck(message);
        Account account = DataCenter.getInstance().getClients().get(message.getSender());
        if (account.getAccountType() != AccountType.ADMIN)
            throw new ClientException("You don't have admin access!");
        Game game = getGame(message.getOnlineGame());
        if (game == null)
            throw new ClientException("Invalid Game");
        game.removeObserver(account);
    }
}
