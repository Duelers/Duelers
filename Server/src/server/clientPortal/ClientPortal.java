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
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

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
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Please press a key to stop the server.");
            reader.readLine();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            server.stop();
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

    synchronized public void sendMessage(String clientName, String message) {//TODO:Change Synchronization
        try {
            if (clients.containsKey(clientName)) {
                clients.get(clientName).getBasicRemote().sendText(message);
            } else {
                GameServer.serverPrint("Client Not Found!");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Set<Map.Entry<String, Session>> getClients() {
        return Collections.unmodifiableSet(clients.entrySet());
    }

    public void removeClient(Session session) {
        clients.remove(session.getId());
    }
}
