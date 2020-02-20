package controller;

import message.Message;

import static models.Constants.SERVER_NAME;

public class InviteController {
    private static InviteController controller;

    private InviteController() {
    }

    public static InviteController getInstance() {
        if (controller == null) {
            controller = new InviteController();
        }
        return controller;
    }

    public void accept(String username) {
        Client.getInstance().addToSendingMessagesAndSend(
                Message.makeAcceptRequestMessage(SERVER_NAME, username)
        );
    }

    public void decline(String username) {
        Client.getInstance().addToSendingMessagesAndSend(Message.makeDeclineRequestMessage(SERVER_NAME, username));
    }
}
