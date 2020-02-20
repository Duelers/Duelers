package message;

public class GetDataMessage {
    private DataName dataName;

    GetDataMessage(DataName dataName) {
        this.dataName = dataName;
    }

    public DataName getDataName() {
        return dataName;
    }
}
