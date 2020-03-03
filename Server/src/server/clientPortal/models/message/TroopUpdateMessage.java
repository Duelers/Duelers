package server.clientPortal.models.message;

import shared.models.game.ServerTroop;

class TroopUpdateMessage {
    private ServerTroop troop;

    TroopUpdateMessage(ServerTroop troop) {
        this.troop = troop;
    }
}
