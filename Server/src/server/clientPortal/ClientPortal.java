package server.clientPortal;

import server.Server;
import server.clientPortal.models.message.Message;
import server.dataCenter.DataCenter;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class ClientPortal extends Thread {
    private static final String CONFIG_PATH = "config";
    private static final int DEFAULT_PORT = 8888;
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
        File file = new File(CONFIG_PATH);
        if (file.exists()) {
            FileReader reader = new FileReader(file);
            StringBuilder portString = new StringBuilder();
            int b;
            while (-1 != (b = reader.read())) {
                portString.append((char) b);
            }
            if (portString.toString().matches("^\\d+$")) {
                return new ServerSocket(Integer.parseInt(portString.toString()));
            }
        }
        return new ServerSocket(DEFAULT_PORT);
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
