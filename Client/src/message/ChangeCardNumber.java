package message;

public class ChangeCardNumber {
    private String cardName;
    private int number;

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
