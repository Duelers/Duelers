package org.projectcardboard.client.models.message;

class GetDataMessage {
    private final DataName dataName;

    GetDataMessage(DataName dataName) {
        this.dataName = dataName;
    }
}
