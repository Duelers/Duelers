package server.clientPortal.models.message;

public class ChangeCardNumber {
    private final String cardName;
    private final int number;

    public ChangeCardNumber(String cardName, int number) {
        this.cardName = cardName;
        this.number = number;
    }

    public String getCardName() {
        return cardName;
    }

    public int getNumber() {
        return number;
    }
}
