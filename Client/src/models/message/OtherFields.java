package models.message;

import models.game.map.Position;

public class OtherFields {
    private String deckName;
    private String cardName;
    private String myCardId;
    private String opponentCardId;
    private String[] myCardIds;
    private Position position;
    private String sudoCommand;

    void setDeckName(String deckName) {
        this.deckName = deckName;
    }

    void setCardName(String cardName) {
        this.cardName = cardName;
    }

    void setMyCardId(String myCardId) {
        this.myCardId = myCardId;
    }

    void setOpponentCardId(String opponentCardId) {
        this.opponentCardId = opponentCardId;
    }

    void setMyCardIds(String[] myCardIds) {
        this.myCardIds = myCardIds;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    void setSudoCommand(String sudoCommand) {
        this.sudoCommand = sudoCommand;
    }
}
