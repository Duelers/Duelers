package server.dataCenter;

import com.google.gson.GsonBuilder;
import server.Server;
import server.clientPortal.ClientPortal;
import server.clientPortal.models.JsonConverter;
import server.clientPortal.models.message.Message;
import server.dataCenter.models.account.Account;
import server.dataCenter.models.account.AccountType;
import server.dataCenter.models.account.Collection;
import server.dataCenter.models.account.TempAccount;
import server.dataCenter.models.card.Card;
import server.dataCenter.models.card.CardType;
import server.dataCenter.models.card.Deck;
import server.dataCenter.models.card.ExportedDeck;
import server.dataCenter.models.db.OldDataBase;
import server.dataCenter.models.sorter.LeaderBoardSorter;
import server.exceptions.ClientException;
import server.exceptions.LogicException;
import server.exceptions.ServerException;
import server.gameCenter.GameCenter;

import java.io.*;
import java.util.*;

public class DataCenter extends Thread {
    private static final String ACCOUNTS_PATH = "Server/resources/accounts";
    private static final String CUSTOM_CARD_PATH = "Server/resources/customCards";
    private static final String[] CARDS_PATHS = {
            "Server/resources/heroCards",
            "Server/resources/minionCards",
            "Server/resources/spellCards",
            "Server/resources/itemCards/collectible",
            "Server/resources/itemCards/usable",
            CUSTOM_CARD_PATH};
    private static final String FLAG_PATH = "Server/resources/itemCards/flag/Flag.item.card.json";
    private static final String STORIES_PATH = "Server/resources/stories";

    private static DataCenter ourInstance = new DataCenter();

    private Map<Account, String> accounts = new HashMap<>();//Account -> ClientName
    private Map<String, Account> clients = new HashMap<>();//clientName -> Account
    private DataBase dataBase = new OldDataBase();

    public static DataCenter getInstance() {
        return ourInstance;
    }

    private DataCenter() {
    }

    @Override
    public void run() {
        Server.getInstance().serverPrint("Starting DataCenter...");
        if (dataBase.isEmpty()) {
            Server.getInstance().serverPrint("Reading Cards...");
            readAllCards();
        }
        Server.getInstance().serverPrint("Reading Accounts...");
        readAccounts();


    }

    public static Card getCard(String cardName, Collection collection) {
        for (Card card : collection.getHeroes()) {
            if (card.getName().equals(cardName))
                return card;
        }
        for (Card card : collection.getMinions()) {
            if (card.getName().equals(cardName))
                return card;
        }
        for (Card card : collection.getSpells()) {
            if (card.getName().equals(cardName))
                return card;
        }
        for (Card card : collection.getItems()) {
            if (card.getName().equals(cardName))
                return card;
        }
        return null;
    }

    public Account getAccount(String username) {
        if (username == null) {
            Server.getInstance().serverPrint("Null Username In getAccount.");
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

    public void register(Message message) throws LogicException {
        if (message.getAccountFields().getUsername() == null || message.getAccountFields().getUsername().length() < 2
                || getAccount(message.getAccountFields().getUsername()) != null) {
            throw new ClientException("Invalid Username!");
        } else if (message.getAccountFields().getPassword() == null || message.getAccountFields().getPassword().length() < 4) {
            throw new ClientException("Invalid Password!");
        } else {
            Account account = new Account(message.getAccountFields().getUsername(), message.getAccountFields().getPassword());
            accounts.put(account, null);
            saveAccount(account);
            Server.getInstance().serverPrint(message.getAccountFields().getUsername() + " Is Created!");

            login(message);

            //give the player all cards upon registration
            //cant give all items because player can only own 3
            Collection originalCards = dataBase.getOriginalCards();

            System.out.println("Starting Heroes");
                for (Card card : originalCards.getHeroes()) {
                    buyAllCards(message, card.getName());

                }

            for(int i = 0; i < 3; i++) {
                System.out.println("Starting Minions");
                for (Card card : originalCards.getMinions()) {
                    //this card is bugged, skipped
                    if(card.getName().equals("Turanian SpearMan")){
                        continue;
                    }
                    buyAllCards(message, card.getName());
                }
                System.out.println("Starting Spells");
                for (Card card : originalCards.getSpells()) {
                    buyAllCards(message, card.getName());
                }

                System.out.println("Starting Items");
                for (Card card : originalCards.getItems()) {
                    buyAllCards(message, card.getName());
                }
            }
        }
    }

    public void login(Message message) throws LogicException {
        if (message.getAccountFields().getUsername() == null || message.getSender() == null) {
            throw new ClientException("invalid message!");
        }
        Account account = getAccount(message.getAccountFields().getUsername());
        if (!ClientPortal.getInstance().hasThisClient(message.getSender())) {
            throw new LogicException("Client Wasn't Added!");
        } else if (account == null) {
            throw new ClientException("Username Not Found!");
        } else if (!account.getPassword().equalsIgnoreCase(message.getAccountFields().getPassword())) {
            throw new ClientException("Incorrect PassWord!");
        } else if (accounts.get(account) != null) {
            throw new ClientException("Selected Username Is Online!");
        } else if (clients.get(message.getSender()) != null) {
            throw new ClientException("Your Client Has Logged In Before!");
        } else {
            accounts.replace(account, message.getSender());
            clients.replace(message.getSender(), account);
            Server.getInstance().addToSendingMessages(Message.makeAccountCopyMessage(message.getSender(), account));
            Server.getInstance().serverPrint(message.getSender() + " Is Logged In");
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

    public void forceLogout(String clientName) throws LogicException {
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
        Server.getInstance().serverPrint(message.getSender() + " Is Logged Out.");
        Server.getInstance().addToSendingMessages(Message.makeDoneMessage(message.getSender()));
    }

    public void createDeck(Message message) throws LogicException {
        loginCheck(message);
        Account account = clients.get(message.getSender());
        account.addDeck(message.getOtherFields().getDeckName());
        Server.getInstance().addToSendingMessages(Message.makeAccountCopyMessage(message.getSender(), account));
        saveAccount(account);
    }

    public void removeDeck(Message message) throws LogicException {
        loginCheck(message);
        Account account = clients.get(message.getSender());
        account.deleteDeck(message.getOtherFields().getDeckName());
        Server.getInstance().addToSendingMessages(Message.makeAccountCopyMessage(message.getSender(), account));
        saveAccount(account);
    }

    public void addToDeck(Message message) throws LogicException {
        loginCheck(message);
        Account account = clients.get(message.getSender());
        account.addCardToDeck(message.getOtherFields().getMyCardId(), message.getOtherFields().getDeckName());
        Server.getInstance().addToSendingMessages(Message.makeAccountCopyMessage(message.getSender(), account));
        saveAccount(account);
    }

    public void removeFromDeck(Message message) throws LogicException {
        loginCheck(message);
        Account account = clients.get(message.getSender());
        account.removeCardFromDeck(message.getOtherFields().getMyCardId(), message.getOtherFields().getDeckName());
        Server.getInstance().addToSendingMessages(Message.makeAccountCopyMessage(message.getSender(), account));
        saveAccount(account);
    }

    public void selectDeck(Message message) throws LogicException {
        loginCheck(message);
        Account account = clients.get(message.getSender());
        account.selectDeck(message.getOtherFields().getDeckName());
        Server.getInstance().addToSendingMessages(Message.makeAccountCopyMessage(message.getSender(), account));
        saveAccount(account);
    }

    public void buyCard(Message message) throws LogicException {

        //System.out.println((message.getOtherFields().getCardName()));
        //System.out.println( dataBase.getOriginalCards().toString() );

        loginCheck(message);
        Account account = clients.get(message.getSender());
        account.buyCard(message.getOtherFields().getCardName(), dataBase.getOriginalCards());
        Server.getInstance().addToSendingMessages(Message.makeAccountCopyMessage(message.getSender(), account));
        saveAccount(account);
    }

    public void buyAllCards(Message message, String cardName) throws LogicException{

        loginCheck(message);
        Account account = clients.get(message.getSender());
        account.buyCard(cardName, dataBase.getOriginalCards());
        Server.getInstance().addToSendingMessages(Message.makeAccountCopyMessage(message.getSender(), account));
        saveAccount(account);

    }

    public void sellCard(Message message) throws LogicException {
        loginCheck(message);
        Account account = clients.get(message.getSender());
        account.sellCard(message.getOtherFields().getMyCardId());
        Server.getInstance().addToSendingMessages(Message.makeAccountCopyMessage(message.getSender(), account));
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

    public List<Card> getCollectibleItems() {
        return Collections.unmodifiableList(dataBase.getCollectibleItems());
    }

    public Collection getNewCustomCards() {
        return dataBase.getNewCustomCards();
    }

    public Card getOriginalFlag() {
        return dataBase.getOriginalFlag();
    }

    public Account[] getLeaderBoard() throws ClientException {
        if (accounts.size() == 0) {
            throw new ClientException("leader board is empty");
        }
        Account[] a = new Account[accounts.keySet().size()];
        Account[] leaderBoard = accounts.keySet().toArray(a);
        Arrays.sort(leaderBoard, new LeaderBoardSorter());
        return leaderBoard;
    }

    public void addCustomCard(Message message) throws LogicException {
        if (!isValidCardName(message.getCard().getCardId()))
            throw new ClientException("invalid name!");
        dataBase.addNewCustomCards(message.getCard());
        saveCustomCard(message.getCard());
        Server.getInstance().sendAddToCustomCardsMessage(message.getCard());
    }

    public void importDeck(Message message) throws LogicException {
        loginCheck(message);
        Account account = clients.get(message.getSender());
        ExportedDeck exportedDeck = message.getExportedDeck();
        Collection collection = account.getCollection();
        Deck deck = collection.extractDeck(exportedDeck);
        account.addDeck(deck);
        Server.getInstance().addToSendingMessages(Message.makeAccountCopyMessage(message.getSender(), account));
        saveAccount(account);
    }

    public void changeCardNumber(String cardName, int changeValue) throws LogicException {
        Card card = getCard(cardName, getOriginalCards());
        if (card == null)
            throw new ClientException("Invalid Card");
        card.setRemainingNumber(card.getRemainingNumber() + changeValue);
        updateCard(card);
        Server.getInstance().sendChangeCardNumberMessage(card);
    }

    public void changeCardNumber(Message message) throws LogicException {
        loginCheck(message);
        Account account = clients.get(message.getSender());
        if (account.getAccountType() != AccountType.ADMIN)
            throw new ClientException("You don't have admin access!");
        changeCardNumber(message.getChangeCardNumber().getCardName(), message.getChangeCardNumber().getNumber());
    }

    public void changeAccountType(Message message) throws LogicException {
        loginCheck(message);
        Account account = clients.get(message.getSender());
        if (account.getAccountType() != AccountType.ADMIN)
            throw new ClientException("You don't have admin access!");
        Account changingAccount = getAccount(message.getChangeAccountType().getUsername());
        if (changingAccount == null)
            throw new ClientException("invalid username!");
        changingAccount.setAccountType(message.getChangeAccountType().getNewType());
        saveAccount(changingAccount);
        Server.getInstance().sendLeaderBoardUpdateMessage(changingAccount);
        Server.getInstance().sendAccountUpdateMessage(changingAccount);
    }

    public void acceptCustomCard(Message message) throws LogicException {
        loginCheck(message);
        Account account = clients.get(message.getSender());
        if (account.getAccountType() != AccountType.ADMIN)
            throw new ClientException("You don't have admin access!");
        Card card = getCard(message.getCardName(), dataBase.getNewCustomCards());
        if (card == null)
            throw new ClientException("invalid card name");
        removeCustomCard(card);
        saveOriginalCard(card);
        dataBase.removeCustomCards(card);
        dataBase.addOriginalCard(card);
        Server.getInstance().sendRemoveCustomCardsMessage(card);
        Server.getInstance().sendAddToOriginalsMessage(card);
    }

    public void rejectCustomCard(Message message) throws LogicException {
        loginCheck(message);
        Account account = clients.get(message.getSender());
        if (account.getAccountType() != AccountType.ADMIN)
            throw new ClientException("You don't have admin access!");
        Card card = getCard(message.getCardName(), dataBase.getNewCustomCards());
        if (card == null)
            throw new ClientException("invalid card name");
        removeCustomCard(card);
        dataBase.removeCustomCards(card);
        Server.getInstance().sendRemoveCustomCardsMessage(card);
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
        Server.getInstance().serverPrint("Accounts Loaded");
    }

    public void readAllCards() {
        for (String path : CARDS_PATHS) {
            File[] files = new File(path).listFiles();
            if (files != null) {
                for (File file : files) {
                    Card card = loadFile(file, Card.class);
                    if (card == null) continue;
                    if (path.equals(CUSTOM_CARD_PATH)) {
                        dataBase.addNewCustomCards(card);
                    } else if (card.getType() == CardType.COLLECTIBLE_ITEM) {
                        dataBase.addNewCollectible(card);
                    } else {
                        dataBase.addOriginalCard(card);
                    }
                }
            }
        }
        dataBase.setOriginalFlag(loadFile(new File(FLAG_PATH), Card.class));
        Server.getInstance().serverPrint("Original Cards Loaded");
    }

    public void saveAccount(Account account) {
        String accountJson = JsonConverter.toJson(new TempAccount(account));
        try {
            FileWriter writer = new FileWriter(ACCOUNTS_PATH + "/" + account.getUsername() + ".account.json");
            writer.write(accountJson);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateCard(Card card) throws ServerException {
        String cardJson = new GsonBuilder().setPrettyPrinting().create().toJson(card);
        for (String path : CARDS_PATHS) {
            File[] files = new File(path).listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.getName().startsWith(card.getName().replaceAll(" ","") + ".")) {
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
        throw new ServerException("Card not found");
    }

    private void saveCustomCard(Card customCard) {
        String cardJson = new GsonBuilder().setPrettyPrinting().create().toJson(customCard);
        try {
            FileWriter writer = new FileWriter(CUSTOM_CARD_PATH + "/" + customCard.getCardId() + ".custom.card.json");
            writer.write(cardJson);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void removeCustomCard(Card card) throws ServerException {
        File[] files = new File(CUSTOM_CARD_PATH).listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.getName().startsWith(card.getCardId() + ".")) {
                    file.delete();
                    return;
                }
            }
        }
        throw new ServerException("Card not found");
    }

    private void saveOriginalCard(Card card) {
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
                case USABLE_ITEM:
                    path = CARDS_PATHS[4] + "/" + card.getCardId() + ".usable.item.card.json";
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
