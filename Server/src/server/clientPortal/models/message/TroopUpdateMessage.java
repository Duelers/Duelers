package server.clientPortal.models.message;

import server.gameCenter.models.game.ServerTroop;

class TroopUpdateMessage {
  private final ServerTroop troop;

  TroopUpdateMessage(ServerTroop troop) {
    this.troop = troop;
  }
}
