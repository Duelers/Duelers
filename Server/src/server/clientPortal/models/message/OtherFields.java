package server.clientPortal.models.message;

import server.gameCenter.models.map.Cell;

public class OtherFields {
    private String deckName;
    private String cardName;
    private String myCardId;
    private String opponentCardId;
    private String[] myCardIds;
    private Cell cell;
    private String sudoCommand;

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

    public Cell getCell() {
        return cell;
    }

    public String getSudoCommand() {
        return sudoCommand;
    }
}
