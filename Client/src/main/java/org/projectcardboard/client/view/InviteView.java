package org.projectcardboard.client.view;

import org.projectcardboard.client.controller.InviteController;
import org.projectcardboard.client.controller.SoundEffectPlayer;
import javafx.scene.layout.AnchorPane;
import shared.models.game.GameType;
import org.projectcardboard.client.models.gui.DialogBox;
import org.projectcardboard.client.models.gui.DialogContainer;
import org.projectcardboard.client.models.gui.DialogText;

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
