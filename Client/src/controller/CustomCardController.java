package controller;

import models.Constants;
import models.card.EditableCard;
import models.exceptions.InputException;
import models.message.Message;

public class CustomCardController {
    private static CustomCardController customCardController;

    public static CustomCardController getInstance() {
        if (customCardController == null) {
            customCardController = new CustomCardController();
        }
        return customCardController;
    }

    public void createCard(EditableCard card) {
        try {
            card.checkValidation();
            Client.getInstance().addToSendingMessagesAndSend(
                    Message.makeCustomCardMessage(Constants.SERVER_NAME, card.toImmutableCard())
            );
        } catch (InputException e) {
            Client.getInstance().getCurrentShow().showError(e.getMessage());
        }
    }
}
