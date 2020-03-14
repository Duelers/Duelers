package server.clientPortal;

import org.glassfish.tyrus.server.Server;
import server.GameServer;
import server.GameEndpoint;
import server.clientPortal.models.message.Message;
import server.dataCenter.DataCenter;
import Config.Config;

import javax.websocket.Session;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class ClientPortal extends Thread {
    private static final ClientPortal ourInstance = new ClientPortal();
    private final HashMap<String, Session> clients = new HashMap<>();

    private ClientPortal() {
    }

    public static ClientPortal getInstance() {
        return ourInstance;
    }

    @Override
    public void run() {
        GameServer.serverPrint("Starting ClientPortal...");
        String port = Config.getInstance().getProperty("PORT");
        int portConverted = Integer.parseInt(port);
        Server server = new Server("localhost", portConverted, "/websockets", GameEndpoint.class);
        try {
            server.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    synchronized public boolean hasThisClient(String clientName) {
        return clients.containsKey(clientName);
    }

    synchronized public void addClient(Session session) {
        clients.put(session.getId(), session);
        DataCenter.getInstance().putClient(session.getId(), null);
    }

    public void addMessage(String clientName, String message) {
        GameServer.addToReceivingMessages(Message.convertJsonToMessage(message));
    }

    synchronized public void sendMessage(Session client, String message) {//TODO:Change Synchronization
        try {
            client.getBasicRemote().sendText(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    synchronized public void prepareAndSendMessage(String clientName, Message message) {
        if (clients.containsKey(clientName)) {
            this.sendMessage(clients.get(clientName), message.toJson());
        } else {
            GameServer.serverPrint("Client Not Found!");
        }
    }

    public void prepareAndSendMessageAsync(String clientName, Message message) {
        CompletableFuture.runAsync(() -> prepareAndSendMessage(clientName, message));
    }

    public Set<Map.Entry<String, Session>> getClients() {
        return Collections.unmodifiableSet(clients.entrySet());
    }

    public void removeClient(Session session) {
        clients.remove(session.getId());
    }
}
