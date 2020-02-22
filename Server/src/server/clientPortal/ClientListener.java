package server.clientPortal;

import server.Server;
import server.dataCenter.DataCenter;
import server.exceptions.LogicException;

import java.net.Socket;
import java.util.Formatter;
import java.util.Scanner;

public class ClientListener extends Thread {
    private Socket socket;

    public ClientListener(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        Scanner scanner;
        Formatter formatter;
        String name = null;
        try {
            Server.serverPrint("New Socket Is Accepted!");
            scanner = new Scanner(socket.getInputStream());
            formatter = new Formatter(socket.getOutputStream());
            formatter.format("#Listening#\n");
            formatter.flush();
            while (true) {
                name = scanner.nextLine().split("#")[1];
                if (name.length() >= 3 && !ClientPortal.getInstance().hasThisClient(name)) {
                    ClientPortal.getInstance().addClient(name, formatter);
                    formatter.format("#Valid#\n");
                    formatter.flush();
                    break;
                } else {
                    formatter.format("#InValid#\n");
                    formatter.flush();
                }
            }
            Server.serverPrint("New Client Is Accepted!");
            while (true) {
                String message = scanner.nextLine();
                ClientPortal.getInstance().addMessage(name, message);
            }
        } catch (Exception e) {
            try {
                DataCenter.getInstance().forceLogout(name);
            } catch (LogicException ex) {
                ex.printStackTrace();
            }
            ClientPortal.getInstance().removeClient(name);
            Server.serverPrint("Client disConnected!");
        }
    }
}
