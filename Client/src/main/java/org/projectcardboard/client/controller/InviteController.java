package org.projectcardboard.client.controller;

import Config.Config;
import org.projectcardboard.client.models.message.Message;

public class InviteController {
    private static InviteController controller;
    private static final String SERVER_NAME = Config.getInstance().getProperty("SERVER_NAME");

    private InviteController() {
    }

    public static InviteController getInstance() {
        if (controller == null) {
            controller = new InviteController();
        }
        return controller;
    }

    public void accept(String username) {
        Client.getInstance().addToSendingMessagesAndSend(Message.makeAcceptRequestMessage(SERVER_NAME, username));
    }

    public void decline(String username) {
        Client.getInstance().addToSendingMessagesAndSend(Message.makeDeclineRequestMessage(SERVER_NAME, username));
    }
}
