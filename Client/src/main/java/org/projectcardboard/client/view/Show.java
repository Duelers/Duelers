package org.projectcardboard.client.view;

import org.projectcardboard.client.controller.Client;
import org.projectcardboard.client.controller.GraphicalUserInterface;
import org.projectcardboard.client.controller.SoundEffectPlayer;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.projectcardboard.client.models.localisation.LanguageData;
import org.projectcardboard.client.models.localisation.LanguageKeys;
import org.projectcardboard.client.models.message.NewGameFields;

public abstract class Show {
    final public AnchorPane root = new AnchorPane();

    private final String ok = LanguageData.getInstance().getValue(new String[] {LanguageKeys.BUTTON_TEXT, LanguageKeys.OK});

    public Show() {
    }

    public void show() {
        GraphicalUserInterface.getInstance().changeScene(root);
        SoundEffectPlayer.getInstance().playSound(SoundEffectPlayer.SoundName.enter_page);
        Client.getInstance().setShow(this);
    }

    public void showError(String message, EventHandler<? super MouseEvent> event) {
        showError(message, ok, event);
    }

    public void showError(String message, String buttonText) {
        showError(message, buttonText, event -> {
        });
    }

    public void showError(String message) {
        // Todo Search Langugage Localisation of Error Message.
        showError(message, ok, event -> {
        });
    }

    public void showError(String message, String buttonText, EventHandler<? super MouseEvent> event) {
        ErrorView errorView = new ErrorView(root);
        errorView.show(message, buttonText, event);
    }

    public void showInvite(NewGameFields newGameFields) {
        InviteView inviteView = new InviteView(root);
        inviteView.show(newGameFields.getGameType(), newGameFields.getOpponentUsername());
    }
}
