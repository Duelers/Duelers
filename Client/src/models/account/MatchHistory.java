package models.account;

public class MatchHistory {
    private String oppName;
    private boolean amIWinner;
    private String date;

    public String getOppName() {
        return this.oppName;
    }

    public String getDate() {
        return date;
    }

    public boolean amIWinner() {
        return amIWinner;
    }
}