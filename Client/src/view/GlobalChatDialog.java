package view;

import controller.Client;
import controller.MainMenuController;
import controller.SoundEffectPlayer;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import models.gui.*;
import server.clientPortal.models.message.ChatMessage;

import static models.gui.UIConstants.SCALE;


public class GlobalChatDialog {
    private static final GlobalChatDialog ourInstance = new GlobalChatDialog();

    private final VBox chatMessages = new VBox();
    private final DialogBox dialogBox = new DialogBox();
    private final NormalField normalField = new NormalField("Message");
    private boolean isOpen;

    private GlobalChatDialog() {
        ScrollPane scrollPane = new ScrollPane(chatMessages);
        OrangeButton sendButton = new OrangeButton("send",
                event -> sendMessage(),
                SoundEffectPlayer.SoundName.select);
        dialogBox.getChildren().addAll(scrollPane, new HBox(normalField, sendButton));
        normalField.setMinSize(500, 50);
        normalField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                sendMessage();
            }
        });
        sendButton.setAlignment(Pos.CENTER_LEFT);
        scrollPane.setMinHeight(800);
        dialogBox.getChildren().stream().filter(node -> node instanceof ScrollPane).forEach(node -> ((ScrollPane) node).setMinHeight(300 * SCALE));
    }

    public static GlobalChatDialog getInstance() {
        return ourInstance;
    }

    private void sendMessage() {
        MainMenuController.getInstance().sendChatMessage(normalField.getText());
        normalField.setText("");
    }

    public void show() {
        if (!isOpen) {
            BackgroundMaker.makeMenuBackgroundFrozen();
            normalField.setText("");
            DialogContainer dialogContainer = new DialogContainer(Client.getInstance().getCurrentShow().root, dialogBox);

            dialogContainer.show();
            dialogBox.makeClosable(dialogContainer, event -> {
                isOpen = false;
                BackgroundMaker.makeMenuBackgroundUnfrozen();
            });
        }
    }

    public void addMessage(ChatMessage chatMessage) {
        chatMessages.getChildren().add(new DialogText(chatMessage.getSenderUsername() + ": " + chatMessage.getText()));
    }
}
