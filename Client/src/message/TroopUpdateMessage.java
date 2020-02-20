package message;

import localGameCenter.models.compressedData.CompressedTroop;
import localGameCenter.models.game.Troop;

public class TroopUpdateMessage {
    private CompressedTroop compressedTroop;

    TroopUpdateMessage(Troop troop) {
        this.compressedTroop = troop.toCompressedTroop();
    }

    public CompressedTroop getCompressedTroop() {
        return compressedTroop;
    }
}
