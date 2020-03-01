package server.clientPortal.models.message;

import shared.models.game.Troop;

class TroopUpdateMessage {
    private Troop troop;

    TroopUpdateMessage(Troop troop) {
        this.troop = troop;
    }
}
