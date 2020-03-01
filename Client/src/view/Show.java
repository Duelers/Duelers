package view;

import controller.Client;
import controller.GraphicalUserInterface;
import controller.SoundEffectPlayer;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import models.languageLocalisation.LanguageData;
import models.message.NewGameFields;

public abstract class Show {
    final public AnchorPane root = new AnchorPane();

    private String ok = LanguageData.getInstance().getValue(new String[] {"BUTTON_TEXT", "OK"});

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
