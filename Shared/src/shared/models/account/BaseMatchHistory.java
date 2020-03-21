package shared.models.account;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BaseMatchHistory {
    protected final String oppName;
    protected final boolean amIWinner;
    protected final Date date;

    protected BaseMatchHistory(String oppName, boolean amIWinner) {
        this.oppName = oppName;
        this.amIWinner = amIWinner;
        this.date = new Date(System.currentTimeMillis());
    }

    public String getOppName() {
        return this.oppName;
    }

    public Date getDate() {
        return date;
    }

    public boolean getAmIWinner() {
        return amIWinner;
    }
}
