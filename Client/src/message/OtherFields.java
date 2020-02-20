package message;

import localGameCenter.models.map.Position;

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

    public String getDeckName() {
        return deckName;
    }

    public String getCardName() {
        return cardName;
    }

    public String getMyCardId() {
        return myCardId;
    }

    public String getOpponentCardId() {
        return opponentCardId;
    }

    public String[] getMyCardIds() {
        return myCardIds;
    }

    public Position getPosition() {
        return position;
    }

    public String getSudoCommand() {
        return sudoCommand;
    }
}
