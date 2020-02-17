package controller;


import javafx.application.Platform;
import models.Constants;
import models.account.AccountInfo;
import models.account.AccountType;
import models.message.ChatMessage;
import models.message.DataName;
import models.message.Message;
import view.GlobalChatDialog;

import static models.Constants.SERVER_NAME;

public class MainMenuController {
    private static MainMenuController ourInstance;

    public static MainMenuController getInstance() {
        if (ourInstance == null) {
            ourInstance = new MainMenuController();
        }
        return ourInstance;
    }

    public void logout() {
        Client.getInstance().addToSendingMessagesAndSend(
                Message.makeLogOutMessage(SERVER_NAME));
    }


    void addChatMessage(ChatMessage chatMessage) {
        Platform.runLater(() -> GlobalChatDialog.getInstance().addMessage(chatMessage));
    }

    public void sendChatMessage(String text) {
        Client.getInstance().addToSendingMessagesAndSend(
                Message.makeChatMessage(Constants.SERVER_NAME, Client.getInstance().getAccount().getUsername(), null, text));
    }

    public void changeAccountTypeRequest(String username, AccountType newValue) {
        Client.getInstance().addToSendingMessagesAndSend(Message.makeChangeAccountTypeMessage(SERVER_NAME, username, newValue));
    }

    public void acceptCustomCard(String cardName) {
        Client.getInstance().addToSendingMessagesAndSend(Message.makeValidateCustomCardMessage(SERVER_NAME, cardName));
    }

    public void rejectCustomCard(String cardName) {
        Client.getInstance().addToSendingMessagesAndSend(
                Message.makeInValidateCustomCardMessage(SERVER_NAME, cardName));
    }
}
