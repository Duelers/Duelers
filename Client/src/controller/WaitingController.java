package controller;

import server.clientPortal.models.message.Message;

import static models.Constants.SERVER_NAME;

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
        Client.getInstance().addToSendingMessagesAndSend(Message.makeCancelRequestMessage(SERVER_NAME));
    }
}
