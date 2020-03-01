package server.clientPortal.models.message;

import shared.models.card.CompressedTroop;
import shared.models.game.Troop;

class TroopUpdateMessage {
    private CompressedTroop compressedTroop;

    TroopUpdateMessage(Troop troop) {
        this.compressedTroop = troop.toCompressedTroop();
    }
}
