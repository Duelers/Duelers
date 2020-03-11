package org.projectcardboard.client.view;

import static org.projectcardboard.client.controller.SoundEffectPlayer.SoundName;

import org.projectcardboard.client.controller.SoundEffectPlayer;
import org.projectcardboard.client.controller.SoundEffectPlayer.SoundName;
import org.projectcardboard.client.models.gui.DialogBox;
import org.projectcardboard.client.models.gui.DialogContainer;
import org.projectcardboard.client.models.gui.DialogText;
import org.projectcardboard.client.models.gui.UIConstants;
import org.projectcardboard.client.models.localisation.LanguageData;
import org.projectcardboard.client.models.localisation.LanguageKeys;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class ErrorView {
    private final AnchorPane root;
    private DialogContainer dialogContainer;

    ErrorView(AnchorPane root) {
        this.root = root;
    }

    public void show(String errorMsg, String buttonText, EventHandler<? super MouseEvent> event) {
        SoundEffectPlayer.getInstance().playSound(SoundName.error);

        String error = LanguageData.getInstance().getValue(new String[] {LanguageKeys.BUTTON_TEXT, LanguageKeys.ERROR});

        DialogText errorLabel = new DialogText(error);
        DialogText errorMessage = new DialogText(errorMsg);

        DialogBox dialogBox = new DialogBox(errorLabel, errorMessage);
        dialogContainer = new DialogContainer(root, dialogBox);

        dialogBox.makeButton(buttonText, e -> {
            event.handle(e);
            dialogContainer.close();
            dialogBox.setCursor(UIConstants.DEFAULT_CURSOR);
        });

        dialogContainer.show();
    }
}
