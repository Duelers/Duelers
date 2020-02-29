package view;

import controller.SoundEffectPlayer;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import models.gui.DialogBox;
import models.gui.DialogContainer;
import models.gui.DialogText;
import models.languageLocalisation.LanguageData;

import static controller.SoundEffectPlayer.SoundName;

public class ErrorView {
    private final AnchorPane root;
    private DialogContainer dialogContainer;

    ErrorView(AnchorPane root) {
        this.root = root;
    }

    public void show(String error, String buttonText, EventHandler<? super MouseEvent> event) {
        SoundEffectPlayer.getInstance().playSound(SoundName.error);

        String errMgs = LanguageData.getInstance().getValue(new String[] {"BUTTON_TEXT", "ERROR"});
        DialogText errorLabel = new DialogText(errMgs);
        DialogText errorMessage = new DialogText(error);

        DialogBox dialogBox = new DialogBox(errorLabel, errorMessage);
        dialogContainer = new DialogContainer(root, dialogBox);

        dialogBox.makeButton(buttonText, e -> {
            event.handle(e);
            dialogContainer.close();
        });

        dialogContainer.show();
    }
}
