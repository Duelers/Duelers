package org.projectcardboard.client.models.message;

public class GameFinishMessage {
    private boolean youWon;

    public boolean amIWinner() {
        return youWon;
    }
}
