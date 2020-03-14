package server.chatCenter;

import server.GameServer;
import server.clientPortal.models.message.Message;
import server.dataCenter.DataCenter;
import server.dataCenter.models.account.Account;

import java.util.ArrayList;

public class ChatCenter {
    private static final ChatCenter ourInstance = new ChatCenter();
    private ArrayList<String> globalMessages = new ArrayList<>();

    private ChatCenter() {
    }

    public static ChatCenter getInstance() {
        return ourInstance;
    }

    public void getMessage(Message message) {
        if (message.getChatMessage().getReceiverUsername() == null) {
            for (Account account : DataCenter.getInstance().getAccounts().keySet()) {//TODO:can has much more performance
                if (DataCenter.getInstance().isOnline(account.getUsername())) {
                    sendMessage(DataCenter.getInstance().getClientName(account.getUsername()),
                            message.getChatMessage().getSenderUsername(), null,
                            message.getChatMessage().getText());
                }
            }
        } else {
                if (message.getChatMessage().getReceiverUsername().equalsIgnoreCase("AI")) {
                    new Thread(() -> {
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException ignored) {
                        }
                        sendMessage(DataCenter.getInstance().getClientName(message.getChatMessage().getSenderUsername()),
                                "AI", message.getChatMessage().getSenderUsername(),
                                "Shut UP :)");
                    }).start();
                } else if(DataCenter.getInstance().isOnline(message.getChatMessage().getReceiverUsername())){
                    sendMessage(DataCenter.getInstance().getClientName(message.getChatMessage().getReceiverUsername()),
                            message.getChatMessage().getSenderUsername(), message.getChatMessage().getReceiverUsername(),
                            message.getChatMessage().getText());
                }
            }

    }

    public void sendMessage(String receiverClientName, String senderUsername, String receiverUsername, String text) {
        if (receiverClientName == null) {
            GameServer.serverPrint("Chat Receiver Error!");
        }
        Message message = Message.makeChatMessage(receiverClientName, senderUsername, receiverUsername, text);
        GameServer.sendMessageAsync(message);
    }
}
