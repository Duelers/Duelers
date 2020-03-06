package models.message;

import shared.models.game.map.Cell;

public class OtherFields {
    private String myCardId;
    private Cell cell;

    void setDeckName(String deckName) {
    }

    void setCardName(String cardName) {
    }

    void setMyCardId(String myCardId) {
        this.myCardId = myCardId;
    }

    void setOpponentCardId(String opponentCardId) {
    }

    void setMyCardIds(String[] myCardIds) {
    }

    public void setCell(Cell cell) {
        this.cell = cell;
    }

    void setSudoCommand(String sudoCommand) {
    }

    public String getMyCardId(){ return myCardId; }

    public Cell getCell(){ return cell; }
}
