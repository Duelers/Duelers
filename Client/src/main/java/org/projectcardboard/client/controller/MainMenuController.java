package org.projectcardboard.client.controller;

import shared.models.account.AccountType;
import org.projectcardboard.client.models.message.ChatMessage;
import org.projectcardboard.client.models.message.Message;
import org.projectcardboard.client.view.GlobalChatDialog;

import Config.Config;
import javafx.application.Platform;

public class MainMenuController {
    private static MainMenuController ourInstance;
    private static final String serverName = Config.getInstance().getProperty("SERVER_NAME");

    public static MainMenuController getInstance() {
        if (ourInstance == null) {
            ourInstance = new MainMenuController();
        }
        return ourInstance;
    }

    public void logout() {
        Client.getInstance().addToSendingMessagesAndSend(Message.makeLogOutMessage(serverName));
    }

    void addChatMessage(ChatMessage chatMessage) {
        Platform.runLater(() -> GlobalChatDialog.getInstance().addMessage(chatMessage));
    }

    public void sendChatMessage(String text) {
        Client.getInstance().addToSendingMessagesAndSend(
                Message.makeChatMessage(serverName, Client.getInstance().getAccount().getUsername(), null, text));
    }

    public void changeAccountTypeRequest(String username, AccountType newValue) {
        Client.getInstance()
                .addToSendingMessagesAndSend(Message.makeChangeAccountTypeMessage(serverName, username, newValue));
    }
}
