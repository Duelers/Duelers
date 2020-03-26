package server.gameCenter.models.game.availableActions;


import server.dataCenter.models.card.ServerCard;
import shared.models.game.availableactions.BaseInsert;

public class Insert extends BaseInsert<ServerCard> {
  Insert(ServerCard card) {
    super(card);
  }
}
