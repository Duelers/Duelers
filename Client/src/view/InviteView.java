package view;

import controller.InviteController;
import controller.SoundEffectPlayer;
import javafx.scene.layout.AnchorPane;
import models.game.GameType;
import models.gui.DialogBox;
import models.gui.DialogContainer;
import models.gui.DialogText;

public class InviteView {
    private final AnchorPane root;
    private DialogContainer dialogContainer;

    InviteView(AnchorPane root) {
        this.root = root;
    }

    public void show(GameType type, String username) {
        SoundEffectPlayer.getInstance().playSound(SoundEffectPlayer.SoundName.error);
        DialogText inviteLabel = new DialogText("Game Invite!");
        DialogText usernameLabel = new DialogText("Invitor username: " + username);
        DialogText gameModeLabel = new DialogText("Game mode: " + type);

        DialogBox dialogBox = new DialogBox(inviteLabel, usernameLabel, gameModeLabel);
        dialogContainer = new DialogContainer(root, dialogBox);

        dialogBox.makeButton("ACCEPT", e -> {
            InviteController.getInstance().accept(username);
            dialogContainer.close();
        });

        dialogBox.makeButton("DECLINE", e -> {
            InviteController.getInstance().decline(username);
            dialogContainer.close();
        });

        dialogContainer.show();
    }
}
