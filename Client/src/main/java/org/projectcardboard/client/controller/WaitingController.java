package org.projectcardboard.client.controller;

import org.projectcardboard.client.models.message.Message;
import Config.Config;

public class WaitingController {
    private static WaitingController controller;

    private WaitingController() {
    }

    public static WaitingController getInstance() {
        if (controller == null) {
            controller = new WaitingController();
        }
        return controller;
    }

    public void cancel() {
        String serverName = Config.getInstance().getProperty("SERVER_NAME");
        Client.getInstance().addToSendingMessagesAndSend(Message.makeCancelRequestMessage(serverName));
    }
}
