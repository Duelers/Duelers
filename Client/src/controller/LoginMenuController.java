package controller;

import Config.Config;
import javafx.application.Platform;
import models.exceptions.InputException;
import models.message.Message;

public class LoginMenuController {
    private static LoginMenuController ourInstance;
    private static final String SERVER_NAME = Config.getInstance().getProperty("SERVER_NAME");

    public static LoginMenuController getInstance() {
        if (ourInstance == null) {
            ourInstance = new LoginMenuController();
        }
        return ourInstance;
    }

    public void register(String userName, String password) {
        try {
            validateUsernameAndPassword(userName, password);
            Client.getInstance().addToSendingMessagesAndSend(
                    Message.makeRegisterMessage(SERVER_NAME, userName, password));
        } catch (InputException e) {
            Platform.runLater(() -> Client.getInstance().getCurrentShow().showError(e.getMessage()));
        }
    }

    private void validateUsernameAndPassword(String userName, String password) throws InputException {
        if (userName == null || userName.length() < 2) {
            throw new InputException("Username is too short!(at least 3 characters)");
        } else if (password == null || password.length() < 4) {
            throw new InputException("Password is too short!(at least 5 characters)");
        }
    }

    public void login(String userName, String password) {
        try {
            validateUsernameAndPassword(userName, password);
            Client.getInstance().addToSendingMessagesAndSend(Message.makeLogInMessage(SERVER_NAME, userName, password));
        } catch (InputException e) {
            Platform.runLater(() -> Client.getInstance().getCurrentShow().showError(e.getMessage()));
        }
    }
}
