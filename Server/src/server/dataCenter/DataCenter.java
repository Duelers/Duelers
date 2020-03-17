package server.dataCenter;

import com.google.gson.GsonBuilder;
import server.GameServer;
import server.clientPortal.ClientPortal;
import server.clientPortal.models.JsonConverter;
import server.clientPortal.models.message.Message;
import server.dataCenter.models.account.Account;
import server.dataCenter.models.account.AccountType;
import server.dataCenter.models.account.Collection;
import server.dataCenter.models.account.TempAccount;
import server.dataCenter.models.card.Deck;
import server.dataCenter.models.card.ExportedDeck;
import server.dataCenter.models.card.ServerCard;
import server.dataCenter.models.db.OldDataBase;
import server.exceptions.ClientException;
import server.exceptions.LogicException;
import server.exceptions.ServerException;
import server.gameCenter.GameCenter;

import javax.websocket.Session;
import java.io.*;
import java.util.*;

public class DataCenter extends Thread {
    private static final String ACCOUNTS_PATH = "Server/resources/accounts";
    private static final String[] CARDS_PATHS = {
            "Server/resources/heroCards",
            "Server/resources/minionCards",
            "Server/resources/spellCards"};

    private static final DataCenter ourInstance = new DataCenter();

    private final Map<Account, String> accounts = new HashMap<>();//Account -> ClientName
    private final Map<String, Account> clients = new HashMap<>();//clientName -> Account
    private final DataBase dataBase = new OldDataBase();

    public static DataCenter getInstance() {
        return ourInstance;
    }

    private DataCenter() {
    }

    @Override
    public void run() {
        GameServer.serverPrint("Starting DataCenter...");
        if (dataBase.isEmpty()) {
            GameServer.serverPrint("Reading Cards...");
            readAllCards();
        }
        GameServer.serverPrint("Reading Accounts...");
        readAccounts();


    }

    public static ServerCard getCard(String cardName, Collection collection) {
        for (ServerCard card : collection.getHeroes()) {
            if (card.getName().equals(cardName))
                return card;
        }
        for (ServerCard card : collection.getMinions()) {
            if (card.getName().equals(cardName))
                return card;
        }
        for (ServerCard card : collection.getSpells()) {
            if (card.getName().equals(cardName))
                return card;
        }
        return null;
    }

    public Account getAccount(String username) {
        if (username == null) {
            GameServer.serverPrint("Null Username In getAccount.");
            return null;
        }
        for (Account account : accounts.keySet()) {
            if (account.getUsername().equalsIgnoreCase(username)) {
                return account;
            }
        }
        return null;
    }

    public boolean isOnline(String username) {
        Account account = getAccount(username);
        if (account == null)
            return false;
        return accounts.get(account) != null;
    }

    public String getClientName(String username) {
        Account account = getAccount(username);
        if (account == null)
            return null;
        return accounts.get(account);
    }

    private void login(Account account, String client) throws LogicException {
        //TODO: send commented out parts async
        if (!ClientPortal.getInstance().hasThisClient(client)) {
            throw new LogicException("Client Wasn't Added!");
        } else if (account == null) {
            throw new ClientException("Username Not Found!");
        } else if (clients.get(client) != null) {
            throw new ClientException("Your Client Has Logged In Before!");
        } else {
            if (accounts.get(account) != null) {
                this.forceLogout(accounts.get(account));
            }
            accounts.replace(account, client);
            clients.replace(client, account);
            
            boolean ownsAllGenerals = account.getCollection().getHeroes().size() == dataBase.getOriginalCards().getHeroes().size();
            boolean ownsAllMinions = (account.getCollection().getMinions().size()/3) == dataBase.getOriginalCards().getMinions().size();
            boolean ownsAllSPells = (account.getCollection().getSpells().size()/3) == dataBase.getOriginalCards().getSpells().size();
            if(!ownsAllGenerals || !ownsAllMinions || !ownsAllSPells) {
                System.out.println("Account " + account.getUsername() + " does not own all cards in current collection. Updating...");
                account.updateCollection(dataBase.getOriginalCards());
                saveAccount(account);
            }
            GameServer.sendMessageAsync(Message.makeAccountCopyMessage(client, account));
            GameServer.serverPrint(client + " Is Logged In");
        }
    }

    private void register(String username, String client) throws LogicException {
        if (username == null || username.length() < 2
                || getAccount(username) != null) {
            throw new ClientException("Invalid Username!");
        } else {
            Account account = new Account(username, "");
            accounts.put(account, null);
            saveAccount(account);
            GameServer.serverPrint(username + " Is Created!");

            login(account, client);
        }
    }

    public void loginOrRegister(String username, String client) {
        Account account = this.getAccount(username);
        try {
            if (account == null) {
                this.register(username, client);
            } else {
                this.login(account, client);
            }
        } catch (LogicException e) {
            GameServer.serverPrint(e.toString());
            GameServer.sendException(e.getMessage(), client);
            //TODO: send error message async
        }
    }

    public void loginCheck(Message message) throws LogicException {
        loginCheck(message.getSender());
    }

    public void loginCheck(String sender) throws LogicException {
        if (sender == null) {
            throw new ClientException("invalid message!");
        }
        if (!clients.containsKey(sender)) {
            throw new LogicException("Client Wasn't Added!");
        }
        if (clients.get(sender) == null) {
            throw new ClientException("Client Was Not LoggedIn");
        }
    }

    public void forceLogout(String clientName) {
        if (clients.get(clientName) != null) {
            GameCenter.getInstance().forceFinishGame(clientName);
            GameCenter.getInstance().removeAllGameRequests(clients.get(clientName));
            accounts.replace(clients.get(clientName), null);
        }
        clients.remove(clientName);
        //TODO(do with logout)
    }

    public void logout(Message message) throws LogicException {
        loginCheck(message);
        GameCenter.getInstance().forceFinishGame(message.getSender());
        GameCenter.getInstance().removeAllGameRequests(clients.get(message.getSender()));
        accounts.replace(clients.get(message.getSender()), null);
        clients.replace(message.getSender(), null);
        GameServer.serverPrint(message.getSender() + " Is Logged Out.");
    }

    public void logout(Session session) throws LogicException {
        String id = session.getId();
        loginCheck(id);
        forceLogout(id);
        GameServer.serverPrint(id + " Is Logged Out.");
    }

    public void createDeck(Message message) throws LogicException {
        loginCheck(message);
        Account account = clients.get(message.getSender());
        account.addDeck(message.getOtherFields().getDeckName());
        GameServer.sendMessageAsync(Message.makeAccountCopyMessage(message.getSender(), account));
        saveAccount(account);
    }

    public void removeDeck(Message message) throws LogicException {
        loginCheck(message);
        Account account = clients.get(message.getSender());
        account.deleteDeck(message.getOtherFields().getDeckName());
        GameServer.sendMessageAsync(Message.makeAccountCopyMessage(message.getSender(), account));
        saveAccount(account);
    }

    public void addToDeck(Message message) throws LogicException {
        loginCheck(message);
        Account account = clients.get(message.getSender());
        account.addCardToDeck(message.getOtherFields().getMyCardId(), message.getOtherFields().getDeckName());
        GameServer.sendMessageAsync(Message.makeAccountCopyMessage(message.getSender(), account));
        saveAccount(account);
    }

    public void removeFromDeck(Message message) throws LogicException {
        loginCheck(message);
        Account account = clients.get(message.getSender());
        account.removeCardFromDeck(message.getOtherFields().getMyCardId(), message.getOtherFields().getDeckName());
        GameServer.sendMessageAsync(Message.makeAccountCopyMessage(message.getSender(), account));
        saveAccount(account);
    }

    public void selectDeck(Message message) throws LogicException {
        loginCheck(message);
        Account account = clients.get(message.getSender());
        account.selectDeck(message.getOtherFields().getDeckName());
        GameServer.sendMessageAsync(Message.makeAccountCopyMessage(message.getSender(), account));
        saveAccount(account);
    }

    public void buyCard(Message message) throws LogicException {

        //System.out.println((message.getOtherFields().getCardName()));
        //System.out.println( dataBase.getOriginalCards().toString() );

        loginCheck(message);
        Account account = clients.get(message.getSender());
        account.buyCard(message.getOtherFields().getCardName(), dataBase.getOriginalCards());
        GameServer.sendMessageAsync(Message.makeAccountCopyMessage(message.getSender(), account));
        saveAccount(account);
    }

    public Map<Account, String> getAccounts() {
        return Collections.unmodifiableMap(accounts);
    }

    public Map<String, Account> getClients() {
        return Collections.unmodifiableMap(clients);
    }

    public void putClient(String name, Account account) {
        clients.put(name, account);
    }

    public Collection getOriginalCards() {
        return dataBase.getOriginalCards();
    }

    public void importDeck(Message message) throws LogicException {
        loginCheck(message);
        Account account = clients.get(message.getSender());
        ExportedDeck exportedDeck = message.getExportedDeck();
        Collection collection = account.getCollection();
        Deck deck = collection.extractDeck(exportedDeck);
        account.addDeck(deck);
        GameServer.sendMessageAsync(Message.makeAccountCopyMessage(message.getSender(), account));
        saveAccount(account);
    }

    public void changeCardNumber(String cardName, int changeValue) throws LogicException {
        ServerCard card = getCard(cardName, getOriginalCards());
        if (card == null)
            throw new ClientException("Invalid ServerCard");
        card.setRemainingNumber(card.getRemainingNumber() + changeValue);
        updateCard(card);
        GameServer.getInstance().sendChangeCardNumberMessage(card);
    }

    public void changeCardNumber(Message message) throws LogicException {
        loginCheck(message);
        Account account = clients.get(message.getSender());
        if (!account.getAccountType().equals(AccountType.ADMIN))
            throw new ClientException("You don't have admin access!");
        changeCardNumber(message.getChangeCardNumber().getCardName(), message.getChangeCardNumber().getNumber());
    }

    public void changeAccountType(Message message) throws LogicException {
        loginCheck(message);
        Account account = clients.get(message.getSender());
        if (!account.getAccountType().equals(AccountType.ADMIN))
            throw new ClientException("You don't have admin access!");
        Account changingAccount = getAccount(message.getChangeAccountType().getUsername());
        if (changingAccount == null)
            throw new ClientException("invalid username!");
        changingAccount.setAccountType(message.getChangeAccountType().getNewType());
        saveAccount(changingAccount);
        GameServer.getInstance().sendAccountUpdateMessage(changingAccount);
    }

    public void readAccounts() {
        File[] files = new File(ACCOUNTS_PATH).listFiles();
        if (files != null) {
            for (File file : files) {
                System.out.println(file.getName());
                TempAccount account = loadFile(file, TempAccount.class);
                if (account == null) continue;
                Account newAccount = new Account(account);
                accounts.put(newAccount, null);
            }
        }
        GameServer.serverPrint("Accounts Loaded");
    }

    public void readAllCards() {
        for (String path : CARDS_PATHS) {
            File[] files = new File(path).listFiles();
            if (files != null) {
                for (File file : files) {
                    ServerCard card = loadFile(file, ServerCard.class);
                    if (card != null) {
                        dataBase.addOriginalCard(card);
                    }
                }
            }
        }
        GameServer.serverPrint("Original Cards Loaded");
    }

    public void saveAccount(Account account) {
        String accountJson = JsonConverter.toPrettyJson(new TempAccount(account));
        try {
            FileWriter writer = new FileWriter(ACCOUNTS_PATH + "/" + account.getUsername() + ".account.json");
            writer.write(accountJson);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateCard(ServerCard card) throws ServerException {
        String cardJson = new GsonBuilder().setPrettyPrinting().create().toJson(card);
        for (String path : CARDS_PATHS) {
            File[] files = new File(path).listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.getName().startsWith(card.getName().replaceAll(" ", "") + ".")) {
                        try {
                            FileWriter writer = new FileWriter(file.getPath());
                            writer.write(cardJson);
                            writer.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return;
                    }
                }
            }
        }
        throw new ServerException("ServerCard not found");
    }

    private void saveOriginalCard(ServerCard card) {
        String cardJson = new GsonBuilder().setPrettyPrinting().create().toJson(card);
        String path;
        try {
            switch (card.getType()) {
                case HERO:
                    path = CARDS_PATHS[0] + "/" + card.getCardId() + ".hero.card.json";
                    break;
                case MINION:
                    path = CARDS_PATHS[1] + "/" + card.getCardId() + ".minion.card.json";
                    break;
                case SPELL:
                    path = CARDS_PATHS[2] + "/" + card.getCardId() + ".spell.card.json";
                    break;
                default:
                    throw new ServerException("Error");
            }
            FileWriter writer = new FileWriter(path);
            writer.write(cardJson);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isValidCardName(String name) {
        for (String path : CARDS_PATHS) {
            File[] files = new File(path).listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.getName().startsWith(name + ".")) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public static <T> T loadFile(File file, Class<T> classOfT) {
        try {
            return JsonConverter.fromJson(new BufferedReader(new FileReader(file)), classOfT);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
