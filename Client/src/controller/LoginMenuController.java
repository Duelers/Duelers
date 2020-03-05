package controller;

import Config.Config;
import javafx.application.Platform;
import models.exceptions.InputException;
import models.languageLocalisation.LanguageData;
import models.message.Message;
import services.RegistrationService;

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
            RegistrationService.getInstance().signUp(userName, password);
        } catch (InputException e) {
            Platform.runLater(() -> Client.getInstance().getCurrentShow().showError(e.getMessage()));
        }
    }

    private void validateUsernameAndPassword(String userName, String password) throws InputException {
        if (userName == null || userName.length() < 2) {
            String errMsg = LanguageData.getInstance().getValue(new String[] {"LOGIN_MENU", "ERROR_SHORT_USERNAME"});
            throw new InputException(errMsg);
        } else if (password == null || password.length() < 4) {
            String errMsg = LanguageData.getInstance().getValue(new String[] {"LOGIN_MENU", "ERROR_SHORT_PASSWORD"});
            throw new InputException(errMsg);
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
