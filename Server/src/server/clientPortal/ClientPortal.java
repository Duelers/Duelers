package server.clientPortal;

import server.Server;
import server.clientPortal.models.message.Message;
import server.dataCenter.DataCenter;
import Config.Config.Config;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class ClientPortal extends Thread {
    private static final ClientPortal ourInstance = new ClientPortal();
    private final HashMap<String, Formatter> clients = new HashMap<>();

    private ClientPortal() {
    }

    public static ClientPortal getInstance() {
        return ourInstance;
    }

    @Override
    public void run() {
        Server.serverPrint("Starting ClientPortal...");
        try {
            ServerSocket serverSocket = makeServerSocket();
            while (true) {
                Socket socket = serverSocket.accept();
                ClientListener clientListener = new ClientListener(socket);
                clientListener.setDaemon(true);
                clientListener.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Server.serverPrint("Error Making ServerSocket!");
            System.exit(-1);
        }
    }

    private ServerSocket makeServerSocket() throws IOException {
        String port = Config.getInstance().getProperty("PORT");
        int portConverted = Integer.parseInt(port);
        return new ServerSocket(portConverted);
    }

    synchronized public boolean hasThisClient(String clientName) {
        return clients.containsKey(clientName);
    }

    synchronized void addClient(String name, Formatter formatter) {//TODO:add remove client
        clients.put(name, formatter);
        DataCenter.getInstance().putClient(name, null);
    }

    void addMessage(String clientName, String message) {
        Server.addToReceivingMessages(Message.convertJsonToMessage(message));
    }

    synchronized public void sendMessage(String clientName, String message) {//TODO:Change Synchronization
        if (clients.containsKey(clientName)) {
            clients.get(clientName).format(message + "\n");
            clients.get(clientName).flush();
        } else {
            Server.serverPrint("Client Not Found!");
        }
    }

    public Set<Map.Entry<String, Formatter>> getClients() {
        return Collections.unmodifiableSet(clients.entrySet());
    }

    void removeClient(String clientName) {
        clients.remove(clientName);
    }
}
