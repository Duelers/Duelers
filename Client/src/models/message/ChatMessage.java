package models.message;

public class ChatMessage {
    private String senderUsername, receiverUsername, text;

    ChatMessage(String senderUsername, String receiverUsername, String text) {
        this.senderUsername = senderUsername;
        this.receiverUsername = receiverUsername;
        this.text = text;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public String getReceiverUsername() {
        return receiverUsername;
    }

    public String getText() {
        return text;
    }
}